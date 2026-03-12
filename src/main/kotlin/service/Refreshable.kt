package service

import entity.Player

/** Refreshable interface. */
interface Refreshable {

  /** refreshes after left shift */
  fun refreshAfterLeftShift(): Unit {}

  /** refreshes after right shift */
  fun refreshAfterRightShift(): Unit {}

  /** refreshes after swap one */
  fun refreshAfterSwapOne(): Unit {}

  /** refreshes after swap all */
  fun refreshAfterSwapAll(): Unit {}

  /** refreshes after next player */
  fun refreshAfterNextPlayer(): Unit {}

  /** refreshes after turn end */
  fun refreshAfterTurnEnd(): Unit {}

  /** refreshes after starting new game */
  fun refreshAfterStartGame(): Unit {}

  /** refreshes after score */
  fun refreshAfterScore(playerScores: List<Pair<Int, Player>>): Unit {}

  /** refreshes after refilling draw stack */
  fun refreshAfterRefillDrawStack(): Unit {}

  /** refreshes logs */
  fun refreshLogs(logEntry: String): Unit {}
}
