package gui

import tools.aqua.bgw.core.BoardGameApplication
import entity.Player
import service.Refreshable
import service.RootService
import gui.*

/**
 * handles button clicks from the main game scene and calls the player action service
 */
class ShiftPokerApplication : BoardGameApplication("Shift Poker"), Refreshable {

    private val rootService = RootService()

    // Scenes
    private val gameScene = ShiftPokerGameScene(rootService).apply{
        swapAllButton.onMouseClicked = {
            rootService.playerActionService.swapAll()
        }
        shiftLeftButton.onMouseClicked = {
            rootService.playerActionService.leftShift()
        }
        shiftRightButton.onMouseClicked = {
            rootService.playerActionService.rightShift()
        }
        skipButton.onMouseClicked = {
            rootService.playerActionService.pass()
        }
        swapOneButton.onMouseClicked = {
            swapOneActive = true
            firstCardSelected = null
            // refreshAllCards() TODO ehm
        }
    }

    private val scoreScene = GameFinishedMenuScene(rootService)

    private val nextPlayerScene = PlayerSwitchMenuScene(rootService).apply {
        nextPlayerButton.onMouseClicked = {
            this@ShiftPokerApplication.hideMenuScene()
            this@ShiftPokerApplication.showGameScene(gameScene)
        }
    }

    private val newGameMenuScene = NewGameMenuScene(rootService)

    init {
        rootService.addRefreshables(
            this,
            gameScene,
            scoreScene,
            nextPlayerScene,
            newGameMenuScene
        )

        this.showMenuScene(newGameMenuScene)
    }

    /**
     * hides menu scene and shows game scene
     */
    override fun refreshAfterNextPlayer() {
        this.hideMenuScene()
        this.showGameScene(gameScene)
    }

    /**
     * handles the end of the turn and draws the label
     */
    override fun refreshAfterTurnEnd() {
        this.showMenuScene(nextPlayerScene)
        nextPlayerScene.apply{
            val game = rootService.getCurrentGame()
            val playerName = game.players[game.currentPlayerIndex % 4].name
            nextPlayerLabel.text = "Spieler $playerName ist dran! \n Mach dich bereit!"
        }
    }
    /**
     * handles the start of the game and draws the label
     */
    
    override fun refreshAfterStartGame() {
        this.hideMenuScene()
        this.showMenuScene(nextPlayerScene)
        nextPlayerScene.apply{
            val game = rootService.getCurrentGame()
            val playerName = game.players[game.currentPlayerIndex % 4].name
            nextPlayerLabel.text = "Spieler $playerName ist dran! \n Mach dich bereit!"
        }
    }

    /**
     * shows score scene and draws the score label
     */
    override fun refreshAfterScore(playerScores: List<Pair<Int, Player>>) {
        this.showMenuScene(scoreScene)
        val scoreStrings = playerScores.map{
                (place, player) -> "$place: ${player.name}"
            }

        scoreScene.apply{ scoreRangPlayers1.text = scoreStrings[0] }
        scoreScene.apply{ scoreRangPlayers2.text = scoreStrings[1] }
        if (scoreStrings.size >= 3) {
            scoreScene.apply{ scoreRangPlayers3.text = scoreStrings[2] }
        }
        if (scoreStrings.size == 4) {
            scoreScene.apply{ scoreRangPlayers4.text = scoreStrings[3] }
        }
    }

}

