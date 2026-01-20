package org.cobalt.api.pathfinder.pathing.heuristic

import kotlin.math.abs
import kotlin.math.sqrt
import org.cobalt.api.pathfinder.pathing.calc.DistanceCalculator
import org.cobalt.api.pathfinder.wrapper.PathPosition

class LinearHeuristicStrategy : IHeuristicStrategy {
  companion object {
    private const val EPSILON = 1e-9
    private const val D1 = 1.0
    private val D2 = sqrt(2.0)
    private val D3 = sqrt(3.0)
  }

  private val perpendicularCalc =
    DistanceCalculator<Double> { progress ->
      InternalHeuristicUtils.calculatePerpendicularDistance(progress)
    }

  private val octileCalc =
    DistanceCalculator<Double> { progress ->
      val dx =
        abs(
          progress.currentPosition().getFlooredX() -
            progress.targetPosition().getFlooredX()
        )
      val dy =
        abs(
          progress.currentPosition().getFlooredY() -
            progress.targetPosition().getFlooredY()
        )
      val dz =
        abs(
          progress.currentPosition().getFlooredZ() -
            progress.targetPosition().getFlooredZ()
        )

      val min = minOf(dx, dy, dz)
      val max = maxOf(dx, dy, dz)
      val mid = dx + dy + dz - min - max

      (D3 - D2) * min + (D2 - D1) * mid + D1 * max
    }

  private val manhattanCalc =
    DistanceCalculator<Double> { progress ->
      val position = progress.currentPosition()
      val target = progress.targetPosition()

      (abs(position.getFlooredX() - target.getFlooredX()) +
        abs(position.getFlooredY() - target.getFlooredY()) +
        abs(position.getFlooredZ() - target.getFlooredZ()))
        .toDouble()
    }

  private val heightCalc =
    DistanceCalculator<Double> { progress ->
      val position = progress.currentPosition()
      val target = progress.targetPosition()

      abs(position.getFlooredY() - target.getFlooredY()).toDouble()
    }

  override fun calculate(context: HeuristicContext): Double {
    val progress = context.getPathfindingProgress()
    val weights = context.heuristicWeights()

    return manhattanCalc.calculate(progress)!! * weights.manhattanWeight +
      octileCalc.calculate(progress)!! * weights.octileWeight +
      perpendicularCalc.calculate(progress)!! * weights.perpendicularWeight +
      heightCalc.calculate(progress)!! * weights.heightWeight
  }

  override fun calculateTransitionCost(from: PathPosition, to: PathPosition): Double {
    val dx = to.getCenteredX() - from.getCenteredX()
    val dy = to.getCenteredY() - from.getCenteredY()
    val dz = to.getCenteredZ() - from.getCenteredZ()

    return sqrt(dx * dx + dy * dy + dz * dz)
  }
}
