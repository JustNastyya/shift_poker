package gui

import tools.aqua.bgw.components.uicomponents.Label
import tools.aqua.bgw.core.Color
import tools.aqua.bgw.core.MenuScene
import tools.aqua.bgw.util.Font
import tools.aqua.bgw.visual.ColorVisual
import service.Refreshable

import gui.GUIParams
import service.*

/**
 * the end game scene
 * has 2 Labels for the `Ranglist` and players scores
 */
class GameFinishedMenuScene(private val rootService: RootService): MenuScene(
    width = 900, height = 800, background = ColorVisual(Color.WHITE)
), Refreshable {
    val gui = GUIParams()
    val scoreRangLabel: Label =
        Label(
            height = gui.titleHeight,
            width = gui.titleWidth,
            posX = 900 / 2 - gui.titleWidth / 2,
            posY = 800 * 0.2,
            text = "Rangliste",
            font = Font(size = 70, fontWeight = Font.FontWeight.BOLD),
            )

    val scoreRangPlayers1: Label =
        Label(
            height = gui.titleHeight * 4,
            width = gui.titleWidth,
            posX = 900 / 2 - gui.titleWidth / 2,
            posY = 800 * 0.3,
            text = "",
            font = Font(size = 40, fontWeight = Font.FontWeight.BOLD))

    val scoreRangPlayers2: Label =
        Label(
            height = gui.titleHeight * 4,
            width = gui.titleWidth,
            posX = 900 / 2 - gui.titleWidth / 2,
            posY = 800 * 0.35,
            text = "",
            font = Font(size = 40, fontWeight = Font.FontWeight.BOLD))

    val scoreRangPlayers3: Label =
        Label(
            height = gui.titleHeight * 4,
            width = gui.titleWidth,
            posX = 900 / 2 - gui.titleWidth / 2,
            posY = 800 * 0.4,
            text = "",
            font = Font(size = 40, fontWeight = Font.FontWeight.BOLD))

    val scoreRangPlayers4: Label =
        Label(
            height = gui.titleHeight * 4,
            width = gui.titleWidth,
            posX = 900 / 2 - gui.titleWidth / 2,
            posY = 800 * 0.45,
            text = "",
            font = Font(size = 40, fontWeight = Font.FontWeight.BOLD))


    init {
        addComponents(
            scoreRangLabel,
            scoreRangPlayers1,
            scoreRangPlayers2,
            scoreRangPlayers3,
            scoreRangPlayers4

            )
    }
}