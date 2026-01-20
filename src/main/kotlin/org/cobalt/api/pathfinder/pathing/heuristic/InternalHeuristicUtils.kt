package org.cobalt.api.pathfinder.pathing.heuristic

import kotlin.math.sqrt
import org.cobalt.api.pathfinder.pathing.PathfindingProgress

internal object InternalHeuristicUtils {
  private const val EPSILON = 1e-9

  fun calculatePerpendicularDistanceSq(progress: PathfindingProgress): Double {
    val s = progress.startPosition()
    val c = progress.currentPosition()
    val t = progress.targetPosition()

    val sx = s.getCenteredX()
    val sy = s.getCenteredY()
    val sz = s.getCenteredZ()
    val cx = c.getCenteredX()
    val cy = c.getCenteredY()
    val cz = c.getCenteredZ()
    val tx = t.getCenteredX()
    val ty = t.getCenteredY()
    val tz = t.getCenteredZ()

    val lineX = tx - sx
    val lineY = ty - sy
    val lineZ = tz - sz
    val lineSq = lineX * lineX + lineY * lineY + lineZ * lineZ

    if (lineSq < EPSILON) {
      val dx = cx - sx
      val dy = cy - sy
      val dz = cz - sz
      return dx * dx + dy * dy + dz * dz
    }

    val toX = cx - sx
    val toY = cy - sy
    val toZ = cz - sz
    val crossX = toY * lineZ - toZ * lineY
    val crossY = toZ * lineX - toX * lineZ
    val crossZ = toX * lineY - toY * lineX
    val crossSq = crossX * crossX + crossY * crossY + crossZ * crossZ

    return crossSq / lineSq
  }

  fun calculatePerpendicularDistance(progress: PathfindingProgress): Double {
    return sqrt(calculatePerpendicularDistanceSq(progress))
  }
}
