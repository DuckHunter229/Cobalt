package org.cobalt.api.pathfinder.pathing.heuristic

import kotlin.math.abs
import kotlin.math.sqrt
import org.cobalt.api.pathfinder.pathing.calc.DistanceCalculator
import org.cobalt.api.pathfinder.wrapper.PathPosition

class SquaredHeuristicStrategy : IHeuristicStrategy {
  companion object {
    private const val EPSILON = 1e-9
    private const val D1 = 1.0
    private val D2 = sqrt(2.0)
    private val D3 = sqrt(3.0)
  }

  private val perpendicularCalc =
    DistanceCalculator<Double> { progress ->
      InternalHeuristicUtils.calculatePerpendicularDistanceSq(progress)
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

      val octile = (D3 - D2) * min + (D2 - D1) * mid + D1 * max
      octile * octile
    }

  private val manhattanCalc =
    DistanceCalculator<Double> { progress ->
      val c = progress.currentPosition()
      val t = progress.targetPosition()

      val manhattan =
        abs(c.getFlooredX() - t.getFlooredX()) +
          abs(c.getFlooredY() - t.getFlooredY()) +
          abs(c.getFlooredZ() - t.getFlooredZ())

      (manhattan * manhattan).toDouble()
    }

  private val heightCalc =
    DistanceCalculator<Double> { progress ->
      val dy =
        progress.currentPosition().getFlooredY() -
          progress.targetPosition().getFlooredY()
      (dy * dy).toDouble()
    }

  override fun calculate(context: HeuristicContext): Double {
    val p = context.getPathfindingProgress()
    val w = context.heuristicWeights()

    return manhattanCalc.calculate(p)!! * w.manhattanWeight +
      octileCalc.calculate(p)!! * w.octileWeight +
      perpendicularCalc.calculate(p)!! * w.perpendicularWeight +
      heightCalc.calculate(p)!! * w.heightWeight
  }

  override fun calculateTransitionCost(from: PathPosition, to: PathPosition): Double {
    val dx = to.getCenteredX() - from.getCenteredX()
    val dy = to.getCenteredY() - from.getCenteredY()
    val dz = to.getCenteredZ() - from.getCenteredZ()

    return dx * dx + dy * dy + dz * dz
  }
}
