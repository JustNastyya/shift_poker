package gui

import tools.aqua.bgw.components.uicomponents.Button
import tools.aqua.bgw.components.uicomponents.Label
import tools.aqua.bgw.components.uicomponents.ListView
import tools.aqua.bgw.components.container.CardStack
import tools.aqua.bgw.util.BidirectionalMap
import tools.aqua.bgw.components.gamecomponentviews.CardView
import tools.aqua.bgw.components.container.LinearLayout
import tools.aqua.bgw.core.Alignment
import tools.aqua.bgw.core.BoardGameScene
import tools.aqua.bgw.core.Color
import tools.aqua.bgw.util.Font
import tools.aqua.bgw.visual.ColorVisual
import entity.Card
import entity.Player
import entity.CardPosition
import service.*
import gui.GUIParams
import gui.CardImageLoader


/**
 * Main game Scene. it has on static elements:
 * 
 * - 2 decks and 3 cards in the middle
 * - 4 times 3 open cards to top/left/right/bottom
 * - hidden cards of the cative player
 * - 5 buttons for players actions
 * - 4 labels for each players name
 * - 1 log screen
 * - 1 label for the number of passed rounds
 * 
 * it also catches refreshes from every action and performs them
 * by redrawing the hole area
 * 
 * and it performs in itselve the checks for swapOne action
 */
class ShiftPokerGameScene(private val rootService: RootService) : BoardGameScene(), Refreshable {
    val gui = GUIParams()
    var swapOneActive = false
    var firstCardSelected: Int? = null

    // --------- info elements ---------
    val roundsLabel: Label =
        Label(
            height = gui.textFieldHeight,
            width = gui.textFieldWidth,
            posX = 0,
            posY = 0,
            text = "-/7",
            font = Font(size = 40),
            visual = ColorVisual(255, 255, 255)
        )

    val logsScreen: ListView<String> =
        ListView<String>(
            width = gui.logsWidth,
            height = gui.logsHeight,
            posX = gui.windowWidth - gui.logsWidth,
            posY = 0,
            visual = ColorVisual(255, 255, 255),
            font = Font(size = 20),
        )

    // ---------- BUTTONS ----------

    val skipButton: Button =
        Button(
            height = gui.buttonHeight,
            width = gui.buttonWidth,
            posX = gui.centerX / 2 - gui.buttonWidth / 2,
            posY = gui.windowHeight - 2 * gui.textFieldSpace - 2 * gui.buttonHeight,
            visual = ColorVisual.BLACK,
            text = "SKIP",
            font = Font(size = 20, color = Color.WHITE))

    val swapAllButton: Button =
        Button(
            height = gui.buttonHeight,
            width = gui.buttonWidth,
            posX = gui.centerX / 2 + gui.textFieldSpace,
            posY = gui.windowHeight - gui.textFieldSpace - gui.buttonHeight,
            visual = ColorVisual.BLACK,
            text = "Swap All",
            font = Font(size = 20, color = Color.WHITE))

    val swapOneButton: Button =
        Button(
            height = gui.buttonHeight,
            width = gui.buttonWidth,
            posX = gui.centerX / 2 - gui.buttonWidth - gui.textFieldSpace,
            posY = gui.windowHeight - gui.textFieldSpace - gui.buttonHeight,
            visual = ColorVisual.BLACK,
            text = "Swap One",
            font = Font(size = 20, color = Color.WHITE))

    val shiftLeftButton: Button =
        Button(
            height = gui.buttonHeight,
            width = gui.buttonWidth,
            posX = gui.windowWidth * 0.75 - gui.buttonWidth,
            posY = gui.windowHeight - gui.textFieldSpace - gui.buttonHeight,
            visual = ColorVisual.BLACK,
            text = "Shift Left",
            font = Font(size = 20, color = Color.WHITE))

    val shiftRightButton: Button =
        Button(
            height = gui.buttonHeight,
            width = gui.buttonWidth,
            posX = gui.windowWidth * 0.75 + gui.textFieldSpace,
            posY = gui.windowHeight - gui.textFieldSpace - gui.buttonHeight,
            visual = ColorVisual.BLACK,
            text = "Shift Right",
            font = Font(size = 20, color = Color.WHITE))

    // --------- card..y stuff ---------

    // middle

    val drawPile = CardStack<CardView>(
        height = gui.cardHeight,
        width = gui.cardWidth,
        posX = gui.centerX - gui.cardWidth * 2.5 - gui.cardSpace * 4,
        posY = gui.centerY - gui.cardHeight,
    )

    val discardPile = CardStack<CardView>(
        height = gui.cardHeight,
        width = gui.cardWidth,
        posX = gui.centerX + gui.cardWidth * 1.5 + gui.cardSpace * 4,
        posY = gui.centerY - gui.cardHeight,
    )

    var flop: LinearLayout<CardView> =
        LinearLayout(
            height = gui.cardHeight,
            width = gui.cardWidth * 3 + gui.cardSpace * 2,
            posX = gui.centerX - gui.cardWidth * 1.5 - gui.cardSpace,
            posY = gui.centerY - gui.cardHeight,
            spacing = gui.cardSpace,
            alignment = Alignment.CENTER)

    // players

    var topPlayerCards: LinearLayout<CardView> =
        LinearLayout(
            height = gui.cardHeight,
            width = gui.cardWidth * 3 + gui.cardSpace * 2,
            posX = gui.centerX - gui.cardWidth * 1.5 - gui.cardSpace,
            posY = gui.cardSpace * 2,
            spacing = gui.cardSpace,
            alignment = Alignment.CENTER)

    var activePlayerOpenCards: LinearLayout<CardView> =
        LinearLayout(
            height = gui.cardHeight,
            width = gui.cardWidth * 3 + gui.cardSpace * 2,
            posX = gui.centerX - gui.cardWidth * 1.5 - gui.cardSpace,
            posY = gui.windowHeight - gui.cardSpace * 2 - gui.cardHeight * 2,
            spacing = gui.cardSpace,
            alignment = Alignment.CENTER)

    var activePlayerHiddenCards: LinearLayout<CardView> =
        LinearLayout(
            height = gui.cardHeight,
            width = gui.cardWidth * 2 + gui.cardSpace,
            posX = gui.centerX - gui.cardWidth - gui.cardSpace / 2,
            posY = gui.windowHeight - gui.cardSpace - gui.cardHeight,
            spacing = gui.cardSpace,
            alignment = Alignment.CENTER)

    var leftPlayerCards: LinearLayout<CardView> =
        LinearLayout(
            height = gui.cardWidth * 3 + gui.cardSpace * 2,
            width = gui.cardHeight,
            posX = gui.cardSpace * 2,
            posY = gui.centerY - gui.cardSpace - gui.cardWidth * 1.5,
            spacing = gui.cardSpace,
            alignment = Alignment.CENTER)

    var rightPlayerCards: LinearLayout<CardView> =
        LinearLayout(
            height = gui.cardWidth * 3 + gui.cardSpace * 2,
            width = gui.cardHeight,
            posX = gui.windowWidth - gui.cardSpace * 2 - gui.cardHeight,
            posY = gui.centerY - gui.cardSpace - gui.cardWidth * 1.5,
            spacing = gui.cardSpace,
            alignment = Alignment.CENTER)

    // ---------------- Player Labels ----------------

    val activePlayerLabel: Label =
        Label(
            height = gui.textFieldHeight / 2,
            width = gui.textFieldWidth / 2,
            posX = gui.centerX - gui.textFieldWidth / 4,
            posY = gui.windowHeight - gui.cardSpace * 4 - gui.cardHeight * 2,
            text = "",
            font = Font(size = 30),
        )

    val topPlayerLabel: Label =
        Label(
            height = gui.textFieldHeight / 2,
            width = gui.textFieldWidth / 2,
            posX = gui.centerX - gui.textFieldWidth / 4,
            posY = gui.cardHeight + gui.cardSpace * 2,
            text = "",
            font = Font(size = 30),
        )

    val leftPlayerLabel: Label =
        Label(
            height = gui.textFieldHeight / 2,
            width = gui.textFieldWidth / 2,
            posX = gui.cardSpace * 2,
            posY = gui.centerY - 2 * gui.cardSpace - gui.cardWidth * 1.5,
            text = "",
            font = Font(size = 30),
        )

    val rightPlayerLabel: Label =
        Label(
            height = gui.textFieldHeight / 2,
            width = gui.textFieldWidth / 2,
            posX = gui.windowWidth - gui.cardSpace * 2 - gui.cardHeight,
            posY = gui.centerY - 2 * gui.cardSpace - gui.cardWidth * 1.5,
            text = "",
            font = Font(size = 30),
        )

    // ------------- init -------------
    private val cardMap: BidirectionalMap<Card, CardView> = BidirectionalMap()

    init {
        background = ColorVisual(108, 168, 59)
        addComponents(
            roundsLabel,
            shiftRightButton,
            shiftLeftButton,
            swapAllButton,
            swapOneButton,
            skipButton,
            logsScreen,
            discardPile,
            drawPile,
            flop,
            topPlayerCards,
            activePlayerOpenCards,
            activePlayerHiddenCards,
            leftPlayerCards,
            rightPlayerCards,
            activePlayerLabel,
            topPlayerLabel,
            leftPlayerLabel,
            rightPlayerLabel
        )
    }
    // ------------- implementing the refreshing services -------------
    
    override fun refreshAfterStartGame() {
        redrawEverything()
    }

    override fun refreshLogs(logEntry: String) {
        logsScreen.items.add(logEntry)
        firstCardSelected = null
        swapOneActive = false
    }

    override fun refreshAfterNextPlayer() {
        redrawEverything()
    }

    override fun refreshAfterLeftShift(){
        redrawEverything()
    }

    override fun refreshAfterRightShift(){
        redrawEverything()
    }

    override fun refreshAfterSwapAll(){
        redrawEverything()
    }

    override fun refreshAfterTurnEnd() {
        redrawEverything()
    }

    override fun refreshAfterSwapOne() {
        redrawEverything()
    }
    
    // ------------- drawing images of cards -------------

    private fun redrawEverything() {
        val game = rootService.getCurrentGame()
        val cardImageLoader = CardImageLoader()

        // refresh swap one var
        firstCardSelected = null
        swapOneActive = false

        // refreshing labels
        val roundsCompleted = (game.rounds * 4 - game.turnsLeft) / 4 + 1
        roundsLabel.text = "$roundsCompleted / ${game.rounds}"

        // refreshing draw stack
        cardMap.clear()
        drawPile.clear()
        game.drawPile.peekAll().reversed().forEach { card ->
            val cardView = CardView(
                height = gui.cardHeight,
                width = gui.cardWidth,
                front = cardImageLoader.frontImageFor(card.suit, card.value),
                back = cardImageLoader.backImage
            )
            cardView.showBack()
            drawPile.add(cardView)
            cardMap.add(card to cardView)
        }
        
        // refreshing discard pile
        discardPile.clear()
        game.discardPile.peekAll().reversed().forEach { card ->
            val cardView = CardView(
                height = gui.cardHeight,
                width = gui.cardWidth,
                front = cardImageLoader.frontImageFor(card.suit, card.value),
                back = cardImageLoader.backImage
            )
            cardView.showFront()
            discardPile.add(cardView)
            cardMap.add(card to cardView)
        }

        // refreshing all open cards
        flop.clear()
        game.flop.forEach { card ->
            refreshOneCard(flop, cardImageLoader, card, isClickable = true)
        }

        refreshAllPlayers(rootService, game.currentPlayerIndex)
    }

    private fun refreshAllPlayers(rootService: RootService, activePlayerIndex: Int) {
        val game = rootService.getCurrentGame()
        val cardImageLoader = CardImageLoader()

        // active player
        val activePlayer = game.players[activePlayerIndex % 4]
        refreshPlayerCards(
            activePlayerOpenCards,
            activePlayer,
            cardImageLoader,
            activePlayerLabel,
            isClickable = true
        )
        refreshPlayerHiddenCards(
            activePlayerHiddenCards,
            activePlayer,
            cardImageLoader,
        )

        // right player
        val rightPlayer = game.players[(activePlayerIndex + 3) % 4]
        refreshPlayerCards(
            rightPlayerCards,
            rightPlayer,
            cardImageLoader,
            rightPlayerLabel
        )

        // top player
        val topPlayer = game.players[(activePlayerIndex + 2) % 4]
        refreshPlayerCards(
            topPlayerCards,
            topPlayer,
            cardImageLoader,
            topPlayerLabel
        )

        // left player
        val leftPlayer = game.players[(activePlayerIndex + 1) % 4]
        refreshPlayerCards(
            leftPlayerCards,
            leftPlayer,
            cardImageLoader,
            leftPlayerLabel
        )
        }

    private fun refreshPlayerCards(
        cardLayout: LinearLayout<CardView>,
        player: Player,
        cardImageLoader: CardImageLoader,
        textLabel: Label,
        isClickable: Boolean = false
    ) {
        cardLayout.clear()

        if (!player.isNone) {
            player.openCards.forEach { card ->
                refreshOneCard(cardLayout, cardImageLoader, card, isClickable)
            }
            textLabel.text = "Player ${player.name}"
        } else {
            textLabel.text = ""
        }
    }

    private fun refreshPlayerHiddenCards(
        cardLayout: LinearLayout<CardView>,
        player: Player,
        cardImageLoader: CardImageLoader,
    ) {
        cardLayout.clear()
        player.handCards.forEach { card ->
            refreshOneCard(cardLayout, cardImageLoader, card)
        }

    }

    private fun refreshOneCard(
        cardLayout: LinearLayout<CardView>,
        cardImageLoader: CardImageLoader,
        card: Card,
        isClickable: Boolean = false
    ) {
        val cardView = CardView(
            height = gui.cardHeight,
            width = gui.cardWidth,
            front = cardImageLoader.frontImageFor(card.suit, card.value),
            back = cardImageLoader.backImage
        )
        cardView.showFront()
        cardLayout.add(cardView)
        cardMap.add(card to cardView)

        val cardPos = cardLayout.components.indexOf(cardView)

        // handling the swapOne action
        cardView.onMouseClicked = {
            if (swapOneActive && isClickable) {
                // first selected card from open active players hand
                if (firstCardSelected == null && cardLayout == activePlayerOpenCards) {
                    firstCardSelected = cardPos
                    cardView.opacity = 0.5
                } else if (firstCardSelected != null && cardLayout == flop) {
                    // handling kotlins paranoya about nulls
                    val firstIndex = firstCardSelected ?: throw IllegalStateException("")

                    swapOneActive = false
                    rootService.playerActionService.swapOne(
                        translateToCardPos(firstIndex),
                        translateToCardPos(cardPos)
                    )

                }
            }

        }
    }

    private fun translateToCardPos(cardPos: Int): CardPosition {
        return when (cardPos) {
            0 -> CardPosition.LEFT
            1 -> CardPosition.MIDDLE
            2 -> CardPosition.RIGHT
            else -> throw IllegalArgumentException("invalid card index")
        }
    }


}