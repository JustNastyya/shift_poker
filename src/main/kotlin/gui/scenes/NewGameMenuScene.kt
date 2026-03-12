package gui

import tools.aqua.bgw.components.uicomponents.Button
import tools.aqua.bgw.components.uicomponents.Label
import tools.aqua.bgw.components.uicomponents.TextArea
import tools.aqua.bgw.components.uicomponents.ComboBox
import tools.aqua.bgw.core.Color
import tools.aqua.bgw.core.MenuScene
import tools.aqua.bgw.util.Font
import tools.aqua.bgw.visual.ColorVisual

import java.util.*

import service.*
import gui.GUIParams

/**
 * the new game scene. (the first one shown)
 * 
 * implements in itself the safe click on the start button
 * by the rule of "first two text fields and the combobox must be filled"
 */
class NewGameMenuScene(private val rootService: RootService): MenuScene(
    width = 1920, height = 1080, background = ColorVisual(Color.WHITE)
), Refreshable {

    private val gui = GUIParams()
    private val titleLabel: Label =
        Label(
            height = gui.titleHeight,
            width = gui.titleWidth,
            posX = gui.centerX - gui.titleWidth / 2,
            posY = 100,
            text = "Schiebe-Poker",
            font = Font(48, fontWeight = Font.FontWeight.BOLD))

    val newGameButton = Button(
        posY = titleLabel.posY + (gui.textFieldSpace + gui.titleHeight) * 4 + 100,
        posX = gui.centerX - gui.buttonWidth / 2,
        height = gui.buttonHeight,
        width = gui.buttonWidth,
        text = "Start the FUN!!!"

    ).apply {
        visual = ColorVisual(Color(136, 221, 136))
        onMouseClicked = {
            updateButtonDisability()
            val players = mutableListOf<String>(
                firstPlayerTextArea.text,
                secondPlayerTextArea.text,
                thirdPlayerTextArea.text,
                fourthPlayerTextArea.text
            )
            val rounds = numRoundsComboBox.selectedItem
            println("PUSHED BUTTON NEW GAME")
            if (rounds != null) {
                rootService.gameService.startGame(players, rounds)
            }
        }
    }
    val firstPlayerTextArea: TextArea = TextArea(
        posX = gui.centerX - gui.textFieldWidth / 2,
        posY = titleLabel.posY + gui.titleHeight + gui.textFieldSpace,
        width = gui.textFieldWidth,
        height = gui.textFieldHeight,
        font = Font(20.0, Color(0x0f141f)),
        prompt = "Spieler 1"
    )
    val secondPlayerTextArea: TextArea = TextArea(
        posX = gui.centerX - gui.textFieldWidth / 2,
        posY = firstPlayerTextArea.posY + gui.textFieldHeight + gui.textFieldSpace,
        width = gui.textFieldWidth,
        height = gui.textFieldHeight,
        font = Font(20.0, Color(0x0f141f)),
        prompt = "Spieler 2"
    )
    val thirdPlayerTextArea = TextArea(
        posX = gui.centerX - gui.textFieldWidth / 2,
        posY = secondPlayerTextArea.posY + gui.textFieldHeight + gui.textFieldSpace,
        width = gui.textFieldWidth,
        height = gui.textFieldHeight,
        font = Font(20.0, Color(0x0f141f)),
        prompt = "Spieler 3 (optional)"
    )
    val fourthPlayerTextArea = TextArea(
        posX = gui.centerX - gui.textFieldWidth / 2,
        posY = thirdPlayerTextArea.posY + gui.textFieldHeight + gui.textFieldSpace,
        width = gui.textFieldWidth,
        height = gui.textFieldHeight,
        font = Font(20.0, Color(0x0f141f)),
        prompt = "Spieler 4 (optional)"
    )
    val numRoundsComboBox = ComboBox<Int>(
        posX = gui.centerX - gui.textFieldWidth / 2,
        posY = fourthPlayerTextArea.posY + gui.textFieldHeight + gui.textFieldSpace + 10,
        width = gui.textFieldWidth,
        height = gui.textFieldHeight,
        visual = ColorVisual(Color(0xffc656)),
        prompt = "Select the number of rounds",
        font = Font(20.0, Color(0x0f141f))
    )

    private fun updateButtonDisability() {
        newGameButton.isDisabled = firstPlayerTextArea.text.isBlank() ||
                secondPlayerTextArea.text.isBlank() ||
                numRoundsComboBox.selectedItem == null
    }

    init {
        numRoundsComboBox.items = mutableListOf(2, 3, 4, 5, 6, 7)
        firstPlayerTextArea.onTextChanged = {updateButtonDisability()}
        secondPlayerTextArea.onTextChanged = {updateButtonDisability()}
        numRoundsComboBox.onItemSelected = {updateButtonDisability()}
        numRoundsComboBox.formatFunction = {
            "$it Runden"
        }
        newGameButton.isDisabled = true
        addComponents(
            titleLabel,
            newGameButton,
            numRoundsComboBox,
            firstPlayerTextArea,
            secondPlayerTextArea,
            thirdPlayerTextArea,
            fourthPlayerTextArea,
        )
    }
}