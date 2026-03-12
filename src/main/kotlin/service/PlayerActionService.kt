package service
import entity.*

/**
 * executes player actions
 */
class PlayerActionService(private val rootService: RootService) : AbstractRefreshingService() {

    /**
     * swaps one card
     * 
     * @param ownPos of `CardPosition` - the position of the active players
     * card which he wants to switch
     * @param midPos of `CardPosition` - the position of middle
     * card which he wants to switch
     * 
     * calls a refresh and a next action
     */
    fun swapOne(ownPos: CardPosition, midPos: CardPosition) {
        val game = rootService.getCurrentGame()
        val ownPosInt = translateCardPosToInt(ownPos)
        val midPosInt = translateCardPosToInt(midPos)

        val ownCard = game.players[game.currentPlayerIndex % 4].openCards[ownPosInt]
        game.players[game.currentPlayerIndex % 4].openCards[ownPosInt] = game.flop[midPosInt]
        game.flop[midPosInt] = ownCard

        // refresh and co
        val playerName = game.players[game.currentPlayerIndex % 4].name
        val log = "Player $playerName has swapped one card"
    
        onAllRefreshables { refreshLogs(log) }
        onAllRefreshables { refreshAfterSwapOne() }
        rootService.gameService.nextAction()
    }

    /**
     * swaps all active players open cards with all middle cards
     * 
     * calls next action
     */
    fun swapAll() {
        val game = rootService.getCurrentGame()

        val middleCardsList = game.flop
        val activePlayersCards = game.players[game.currentPlayerIndex % 4].openCards

        game.flop = activePlayersCards
        game.players[game.currentPlayerIndex % 4].openCards = middleCardsList
        
        // refresh and co
        val playerName = game.players[game.currentPlayerIndex % 4].name
        val log = "Player $playerName has swapped all cards"
    
        onAllRefreshables { refreshLogs(log) }
        onAllRefreshables { refreshAfterSwapAll() }
        rootService.gameService.nextAction()
    }

    /**
     * implements the `pass`. calls next action
     */
    fun pass(){
        // refresh and co
        val game = rootService.getCurrentGame()
        val playerName = game.players[game.currentPlayerIndex % 4].name
        val log = "Player $playerName has passt"
    
        onAllRefreshables { refreshLogs(log) }
        onAllRefreshables { refreshAfterSwapAll() } // TODO we sont have refresh after pass
        rootService.gameService.nextAction()
    }

    /**
     * implements a left shift. removes the left card
     * from the middle cards and puts it into the discard pile.
     * then takes a card from the draw Stack and puts it to the right
     * 
     * before performing checks if the draw stack is empty and refills it if needed
     * 
     * calls next action
     */
    fun leftShift() {
        val game = rootService.getCurrentGame()

        if (game.drawPile.empty) {
            rootService.gameService.refillDrawStack()
        }

        val leftCard = game.flop.removeAt(0)
        game.discardPile.putOnTop(leftCard)
        val newCard = game.drawPile.draw()
        game.flop.add(2, newCard)
        
        // refresh and co
        val playerName = game.players[game.currentPlayerIndex % 4].name
        val log = "Player $playerName has shifted left"
    
        onAllRefreshables { refreshLogs(log) }
        onAllRefreshables { refreshAfterLeftShift() }
        rootService.gameService.nextAction()
    }

    /**
     * implements a right shift. removes the right card
     * from the middle cards and puts it into the discard pile.
     * then takes a card from the draw Stack and puts it to the left
     * 
     * before performing checks if the draw stack is empty and refills it if needed
     * 
     * calls next action
     */
    fun rightShift() {
        val game = rootService.getCurrentGame()

        // check if deck is empty
        if (game.drawPile.empty) {
            rootService.gameService.refillDrawStack()
        }
        // TODO changed the indexes. they were iinda false
        val leftCard = game.flop.removeAt(2)
        game.discardPile.putOnTop(leftCard)
        val newCard = game.drawPile.draw()
        game.flop.add(0, newCard)

        // refresh and co
        val playerName = game.players[game.currentPlayerIndex % 4].name
        val log = "Player $playerName has shifted right"
    
        onAllRefreshables { refreshLogs(log) }
        onAllRefreshables { refreshAfterRightShift() }
        rootService.gameService.nextAction()

    }

    /**
     * transtales CardPosition to int (index of the middle cards list)
     * 
     * @param cardPos of `CardPosition` - the position to translate
     * 
     * returns a number of {0, 1, 2}
     */
    private fun translateCardPosToInt(cardPos: CardPosition): Int {
        return when (cardPos) {
            CardPosition.LEFT -> 0
            CardPosition.MIDDLE -> 1
            CardPosition.RIGHT -> 2
        }
    }

}