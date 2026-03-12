package service
import entity.*


/**
 * [Refreshable] implementation that refreshes nothing, but remembers
 * if a refresh method has been called (since last [reset])
 */
class TestRefreshable: Refreshable {

    var refreshAfterLeftShiftCalled: Boolean = false
        private set

    var refreshAfterRightShiftCalled: Boolean = false
        private set

    var refreshAfterSwapOneCalled: Boolean = false
        private set

    var refreshAfterSwapAllCalled: Boolean = false
        private set

    var refreshAfterNextPlayerCalled: Boolean = false
        private set
    
    var refreshAfterTurnEndCalled: Boolean = false
        private set

    var refreshAfterStartGameCalled: Boolean = false
        private set

    var refreshAfterScoreCalled: Boolean = false
        private set

    var refreshAfterRefillDrawStackCalled: Boolean = false
        private set

    /**
     * resets all *Called properties to false
     */
    fun reset() {
        refreshAfterLeftShiftCalled = false
        refreshAfterRightShiftCalled = false
        refreshAfterSwapOneCalled = false
        refreshAfterSwapAllCalled = false
        refreshAfterNextPlayerCalled = false
        refreshAfterTurnEndCalled = false
        refreshAfterStartGameCalled = false
        refreshAfterScoreCalled = false
        refreshAfterRefillDrawStackCalled = false
    }

    override fun refreshAfterLeftShift() {
        refreshAfterLeftShiftCalled = true
    }

    override fun refreshAfterNextPlayer() {
        refreshAfterNextPlayerCalled = true
    }

    override fun refreshAfterRightShift() {
        refreshAfterRightShiftCalled = true
    }

    override fun refreshAfterSwapOne() {
        refreshAfterSwapOneCalled = true
    }

    override fun refreshAfterSwapAll() {
        refreshAfterSwapAllCalled = true
    }

    override fun refreshAfterTurnEnd() {
        refreshAfterTurnEndCalled = true
    }

    override fun refreshAfterStartGame() {
        refreshAfterStartGameCalled = true
    }

    override fun refreshAfterScore(playerScores: List<Pair<Int, Player>>) {
        refreshAfterScoreCalled = true
    }

    override fun refreshAfterRefillDrawStack() {
        refreshAfterRefillDrawStackCalled = true
    }


}