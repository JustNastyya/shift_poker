package service

import entity.ShiftPoker

/**
 * Root service. the hub for all other services.
 * 
 * stores `GameService` and `PlayerActionService`
 */
class RootService {
    val gameService = GameService(this)
    val playerActionService = PlayerActionService(this)
    var game: ShiftPoker? = null

    /**
     * a helper function used to go around kotlins
     * nullable things. returns `game` of `ShiftPokerGame`
     */
    fun getCurrentGame(): ShiftPoker = game?: throw IllegalStateException("No game yet")

    /**
     * add a new refreshable to the refreshable lists
     * of game service and player action service
     */
    fun addRefreshable(newRefreshable: Refreshable) {
        gameService.addRefreshable(newRefreshable)
        playerActionService.addRefreshable(newRefreshable)
    }

    /**
     * adds many refreshables to the refreshable lists
     * of game service and player action service
     */
    fun addRefreshables(vararg newRefreshables: Refreshable) {
        newRefreshables.forEach { addRefreshable(it) }
    }

}