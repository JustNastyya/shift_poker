package gui

import tools.aqua.bgw.components.uicomponents.Button
import tools.aqua.bgw.components.uicomponents.Label
import tools.aqua.bgw.core.Color
import tools.aqua.bgw.core.MenuScene
import tools.aqua.bgw.util.Font
import tools.aqua.bgw.visual.ColorVisual

import java.util.*

import service.*
import gui.*

/**
 * the switch between players. has a button which calls the game scene
 */
class PlayerSwitchMenuScene(private val rootService: RootService): MenuScene(
    width = 1920, height = 1080, background = ColorVisual(Color.WHITE)
), Refreshable {
    var gui = GUIParams()

    val nextPlayerLabel: Label =
        Label(
            height = gui.titleHeight * 2,
            width = gui.titleWidth * 2,
            posX = gui.centerX - gui.titleWidth,
            posY = 100,
            text = "",
            font = Font(48, fontWeight = Font.FontWeight.BOLD))

    val nextPlayerButton = Button(
        posY = nextPlayerLabel.posY + gui.titleHeight * 2 + gui.textFieldSpace,
        posX = gui.centerX - gui.buttonWidth,
        height = gui.buttonHeight * 2,
        width = gui.buttonWidth * 2,
        text = "OK",
        font = Font(48, fontWeight = Font.FontWeight.BOLD)
    ).apply {
        visual = ColorVisual(Color(136, 221, 136))
        onMouseClicked = {
            rootService.gameService.showGameScene()
        }
    }
    init {
        addComponents(
            nextPlayerLabel,
            nextPlayerButton,

        )
    }
}