package service

import entity.*
/**
 * orchestrates over game states. is used to initialize game, implement next action and compute game scores
 * @param rootService - root service
 */
class GameService(private val rootService: RootService): AbstractRefreshingService() {

    /**
     * initializes the game. creates decks, hands out players cards, creates variable game
     * calls afterwards reshresh after game start. in the end refreshes services AfterStartGame
     * @param playerNames - list of players lists as the were in 4 text fields in frontend
     * @param rounds - number of rounds from frontend
     */
    fun startGame(
        playerNames: List<String>,
        rounds: Int
    ) {
        // checks
        if (playerNames[0] == "" || playerNames[1] == "") {
            throw IllegalArgumentException("players 0 and 1 must exist")
        }
        if (rounds < 2 || rounds > 7) {
            throw IllegalArgumentException("rounds must be between 2 and 7")
        }
        // Card stuff
        val drawPile = CardStack(defaultRandomCardList())

        val discardPile = CardStack(emptyList())
        val flop = drawPile.drawMany(3)

        val players: MutableList<Player> = mutableListOf()

        for (playerName in playerNames) {
            if (playerName.isBlank()) {
                players.add(
                    Player(
                        playerName,
                        true
                    )
                )
            } else {
                players.add(
                    Player(
                        playerName,
                        false,
                        drawPile.drawMany(2),
                        drawPile.drawMany(3)
                    )
                )
            }
        }

        // in case of 2 players, they must sit opponed to each other
        if (players[2].isNone && players[3].isNone) {
            // switch players 1 and 2
            val player1 = players[1]
            players[1] = players[2]
            players[2] = player1
        }
        
        // random player index. but not the nan player!
        val validPlayers = players.withIndex().filter{!it.value.isNone}
            .map{it.index}

        rootService.game = ShiftPoker(
            rounds = rounds,
            actionsLeft = 2,
            turnsLeft = 4 * rounds,
            currentPlayerIndex = validPlayers.random(),
            players = players,
            drawPile = drawPile,
            discardPile = discardPile,
            flop=flop,
        )

        onAllRefreshables { refreshAfterStartGame() }
    }

    /**
     * implements next action. decrements actions left. checks for left action for current
     * user. can call nextTurn 
     */
    fun nextAction() {
        val g = rootService.getCurrentGame()
        g.actionsLeft--

        if (g.actionsLeft == 0) {
            nextPlayer()
        }

    }

    /**
     * implements next turn functionallity. 
     * decreases turns left and increases the current player index 
     * (possibly twice if next player isNone)
     * 
     * can either cann endGame or a refresh after turn end
     */
    fun nextPlayer () {
        val g = rootService.getCurrentGame()
        
        g.turnsLeft--
        g.currentPlayerIndex++
        g.actionsLeft = 2

        // since there will never be 2 None players sitting
        // near each other, this works
        if (g.players[g.currentPlayerIndex % 4].isNone) {
            g.turnsLeft--
            g.currentPlayerIndex++
        }
        if (g.turnsLeft == 0) {
            score()
        } else {
            onAllRefreshables { refreshAfterTurnEnd() }
        }
    }

    /**
     * refills the draw stack from the discard pile
     */
    fun refillDrawStack() {
        val g = rootService.getCurrentGame()

        g.discardPile.shuffle()
        g.drawPile = g.discardPile
        g.discardPile = CardStack(emptyList())
    }

    /**
     * calls the score compute calculator and computes score
     * refreshes after score
     */
    private fun score() {
        val sc = ScoreCalculator()
        val g = rootService.getCurrentGame()
        val gameScore = sc.getGameScore(g.players)
        onAllRefreshables { refreshAfterScore(gameScore) }
    }

    /**
     * calls refresh for next player. is used only after initializing the game
     */
    fun showGameScene() {
        onAllRefreshables { refreshAfterNextPlayer() }
    }

    /**
     * returns a `List<Card>` containing all cards in the deck
     */
    private fun defaultRandomCardList() = List(52) { index ->
        Card(
            CardValue.values()[(index % 13)],
            CardSuit.values()[index / 13]
            )
    }.shuffled()

}