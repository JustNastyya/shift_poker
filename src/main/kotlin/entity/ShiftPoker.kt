package entity

/** data class implementing all important game relevant info
 * about the game
 * @param rounds: hole number of rounds
 * @param actionsLeft : actions left for active player
 * @param turnsLeft : hole turns left for the game
 * @param currentPlayerIndex :  of the current player
 * @param players : of all players
 * @param drawPile : draw stack
 * @param discardPile : discard pile
 * @param flop : 3 cards in the middle
 * */
data class ShiftPoker (
    var rounds: Int = 0,
    var actionsLeft: Int = 0,
    var turnsLeft: Int = 0,
    var currentPlayerIndex: Int = 0,
    var players: List<Player> = MutableList<Player>(4, { i -> Player("") }),
    var drawPile: CardStack,
    var discardPile: CardStack,
    var flop: MutableList<Card>
)
