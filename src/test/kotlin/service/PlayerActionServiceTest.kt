package service
import entity.CardSuit
import entity.CardValue
import entity.Card
import entity.CardStack
import entity.CardPosition
import kotlin.test.*

/**
 * tests player action service. uses root service
 */
class PlayerActionServiceTest {
    /**
     * The [RootService] is initialized in the [setUpGame] function
     * hence it is a late-initialized property.
     */
    private lateinit var rootService: RootService

    /**
     * This [setUpGame] function is executed before every test
     * due to the [BeforeTest] annotation.
     * starts a game with a static order of cards that can be used
     * in other tests to deterministically validate the outcome
     * of turns.
     *
     */
    @BeforeTest
    fun setUpGame() {
        rootService = RootService()
    }


    /**
     * tests swap one method from different players and different positions
     *  + the calling of next player afterwards
     */
    @Test
    fun testSwapOne() {
        rootService.gameService.startGame(listOf("a", "b", "", ""), 2)
        val testRefreshable = TestRefreshable()
        rootService.addRefreshable(testRefreshable)
        val game = rootService.getCurrentGame()
        val curTurnsLeft = game.turnsLeft
        game.currentPlayerIndex = 0

        game.flop = mutableListOf(
            Card(CardValue.FOUR, CardSuit.CLUBS),
            Card(CardValue.TWO, CardSuit.CLUBS),
            Card(CardValue.THREE, CardSuit.CLUBS),
            
        )
        game.players[game.currentPlayerIndex % 4].openCards = mutableListOf(
            Card(CardValue.FOUR, CardSuit.SPADES),
            Card(CardValue.TWO, CardSuit.SPADES),
            Card(CardValue.THREE, CardSuit.SPADES)
        )
        
        // test swap one from the middle
        rootService.playerActionService.swapOne(CardPosition.MIDDLE, CardPosition.MIDDLE)
        assertEquals(
            game.flop,
            listOf(
                Card(CardValue.FOUR, CardSuit.CLUBS),
                Card(CardValue.TWO, CardSuit.SPADES),
                Card(CardValue.THREE, CardSuit.CLUBS),
            )
        )
        assertEquals(
            game.players[game.currentPlayerIndex % 4].openCards,
            listOf(
                Card(CardValue.FOUR, CardSuit.SPADES),
                Card(CardValue.TWO, CardSuit.CLUBS),
                Card(CardValue.THREE, CardSuit.SPADES),
            )
        )
        assertEquals(game.actionsLeft, 1)

        // test swap one from 0 and 2
        rootService.playerActionService.swapOne(CardPosition.LEFT, CardPosition.RIGHT)
        assertEquals(
            game.flop,
            listOf(
                Card(CardValue.FOUR, CardSuit.CLUBS),
                Card(CardValue.TWO, CardSuit.SPADES),
                Card(CardValue.FOUR, CardSuit.SPADES),
            )
        )
        assertEquals(
            game.players[(game.currentPlayerIndex - 2) % 4].openCards,
            listOf(
                Card(CardValue.THREE, CardSuit.CLUBS),
                Card(CardValue.TWO, CardSuit.CLUBS),
                Card(CardValue.THREE, CardSuit.SPADES),
            )
        )
        assertEquals(game.actionsLeft, 2)
        assertEquals(game.currentPlayerIndex, 2)
        assertEquals(game.turnsLeft, curTurnsLeft - 2)

        game.players[game.currentPlayerIndex % 4].openCards = mutableListOf(
            Card(CardValue.FOUR, CardSuit.DIAMONDS),
            Card(CardValue.TWO, CardSuit.DIAMONDS),
            Card(CardValue.THREE, CardSuit.DIAMONDS)
        )
        // test swap one from 2 and 0
        rootService.playerActionService.swapOne(CardPosition.RIGHT, CardPosition.LEFT)
        assertEquals(
            game.flop,
            listOf(
                Card(CardValue.THREE, CardSuit.DIAMONDS),
                Card(CardValue.TWO, CardSuit.SPADES),
                Card(CardValue.FOUR, CardSuit.SPADES),
            )
        )
        assertEquals(
            game.players[game.currentPlayerIndex % 4].openCards,
            listOf(
                Card(CardValue.FOUR, CardSuit.DIAMONDS),
                Card(CardValue.TWO, CardSuit.DIAMONDS),
                Card(CardValue.FOUR, CardSuit.CLUBS),
            )
        )
        assertEquals(game.actionsLeft, 1)
        assertEquals(game.currentPlayerIndex, 2)
        assertEquals(game.players[game.currentPlayerIndex % 4].name, "b")
        assertEquals(game.turnsLeft, curTurnsLeft - 2)
    }

    /**
     * tests swap all 
     *  + the calling of next player afterwards
     */
    @Test
    fun testSwapAll() {
        rootService.gameService.startGame(listOf("a", "b", "", ""), 2)
        val testRefreshable = TestRefreshable()
        rootService.addRefreshable(testRefreshable)
        val game = rootService.getCurrentGame()

        game.flop = mutableListOf(
            Card(CardValue.FOUR, CardSuit.CLUBS),
            Card(CardValue.TWO, CardSuit.CLUBS),
            Card(CardValue.THREE, CardSuit.CLUBS),
            
        )
        game.players[game.currentPlayerIndex % 4].openCards = mutableListOf(
            Card(CardValue.FOUR, CardSuit.SPADES),
            Card(CardValue.TWO, CardSuit.SPADES),
            Card(CardValue.THREE, CardSuit.SPADES)
        )
        
        // test swap one from the middle
        rootService.playerActionService.swapAll()
        assertEquals(
            game.flop,
            listOf(
                Card(CardValue.FOUR, CardSuit.SPADES),
                Card(CardValue.TWO, CardSuit.SPADES),
                Card(CardValue.THREE, CardSuit.SPADES)
            )
        )
        assertEquals(
            game.players[game.currentPlayerIndex % 4].openCards,
            listOf(
                Card(CardValue.FOUR, CardSuit.CLUBS),
                Card(CardValue.TWO, CardSuit.CLUBS),
                Card(CardValue.THREE, CardSuit.CLUBS),
            )
        )
        assertEquals(
            game.actionsLeft,
            1
        )
    }

    /**
     * tests pass + the calling of next player afterwards
     */
    @Test
    fun testPass() {
        rootService.gameService.startGame(listOf("a", "b", "", ""), 2)
        val testRefreshable = TestRefreshable()
        rootService.addRefreshable(testRefreshable)
        val game = rootService.getCurrentGame()
        val curTurnsLeft = game.turnsLeft
        game.currentPlayerIndex = 0

        
        // test swap one from the middle
        rootService.playerActionService.pass()
        assertEquals(
            game.actionsLeft,
            1
        )

        // test swap one from 0 and 2
        rootService.playerActionService.pass()
        assertEquals(
            game.actionsLeft,
            2
        )
        assertEquals(
            game.currentPlayerIndex,
            2
        )
        assertEquals(
            game.turnsLeft,
            curTurnsLeft - 2
        )
    }

    /**
     * tests leftshift including the case of empty deck
     *  + the calling of next player afterwards
     */
    @Test
    fun testLeftShift() {
        rootService.gameService.startGame(listOf("a", "b", "", ""), 2)
        val testRefreshable = TestRefreshable()
        rootService.addRefreshable(testRefreshable)
        val game = rootService.getCurrentGame()
        val curTurnsLeft = game.turnsLeft
        game.currentPlayerIndex = 0

        game.drawPile = CardStack(mutableListOf(
            Card(CardValue.FOUR, CardSuit.CLUBS),
            Card(CardValue.TWO, CardSuit.CLUBS),
        ))
        game.discardPile = CardStack(emptyList())
        game.flop = mutableListOf(
            Card(CardValue.TWO, CardSuit.DIAMONDS),
            Card(CardValue.THREE, CardSuit.DIAMONDS),
            Card(CardValue.FOUR, CardSuit.DIAMONDS),
            )
        // test swap one from the middle
        rootService.playerActionService.leftShift()
        assertEquals(
            game.drawPile.peekAll(), CardStack(listOf(
                Card(CardValue.TWO, CardSuit.CLUBS),
            )).peekAll()
        )
        assertEquals(
            game.discardPile.peekAll(),
            CardStack(listOf(
                Card(CardValue.TWO, CardSuit.DIAMONDS),
            )).peekAll()
        )
        assertEquals(game.actionsLeft, 1)

        // test swap one from 0 and 2
        // test swap one from the middle
        rootService.playerActionService.leftShift()
        assertEquals(
            game.drawPile.peekAll(),
            CardStack(emptyList()).peekAll()
        )
        assertEquals(
            game.discardPile.peekAll(),
            CardStack(listOf(
                Card(CardValue.THREE, CardSuit.DIAMONDS),
                Card(CardValue.TWO, CardSuit.DIAMONDS),
            )).peekAll()
        )

        assertEquals(game.actionsLeft, 2)
        assertEquals(game.currentPlayerIndex, 2)
        assertEquals(game.turnsLeft, curTurnsLeft - 2)

        // test swap one from 0 and 2
        // test swap one from the middle
        rootService.playerActionService.leftShift()

        assertEquals(game.actionsLeft, 1)
        assertEquals(game.currentPlayerIndex, 2)
        assertEquals(game.players[game.currentPlayerIndex % 4].name, "b")
        assertEquals(game.turnsLeft, curTurnsLeft - 2)
    }

    /**
     * tests rightshift including the case of empty deck
     *  + the calling of next player afterwards
     */
    @Test
    fun testRightShift() {
        rootService.gameService.startGame(listOf("a", "b", "", ""), 2)
        val testRefreshable = TestRefreshable()
        rootService.addRefreshable(testRefreshable)
        val game = rootService.getCurrentGame()
        val curTurnsLeft = game.turnsLeft
        game.currentPlayerIndex = 0

        game.drawPile = CardStack(mutableListOf(
            Card(CardValue.FOUR, CardSuit.CLUBS),
            Card(CardValue.TWO, CardSuit.CLUBS),
        ))
        game.discardPile = CardStack(emptyList())
        game.flop = mutableListOf(
            Card(CardValue.TWO, CardSuit.DIAMONDS),
            Card(CardValue.THREE, CardSuit.DIAMONDS),
            Card(CardValue.FOUR, CardSuit.DIAMONDS),
        )
        
        // test swap one from the middle
        rootService.playerActionService.rightShift()
        assertEquals(
            game.drawPile.peekAll(),
            CardStack(listOf(
                Card(CardValue.TWO, CardSuit.CLUBS),
            )).peekAll()
        )
        assertEquals(
            game.discardPile.peekAll(),
            CardStack(listOf(
                Card(CardValue.FOUR, CardSuit.DIAMONDS),
            )).peekAll()
        )
        assertEquals(game.actionsLeft, 1)

        // test swap one from 0 and 2
        // test swap one from the middle
        rootService.playerActionService.rightShift()
        assertEquals(
            game.drawPile.peekAll(),
            CardStack(emptyList()).peekAll()
        )
        assertEquals(
            game.discardPile.peekAll(),
            CardStack(listOf(
                Card(CardValue.THREE, CardSuit.DIAMONDS),
                Card(CardValue.FOUR, CardSuit.DIAMONDS),
                )).peekAll()
        )
        assertEquals(game.actionsLeft, 2)
        assertEquals(game.currentPlayerIndex, 2)
        assertEquals(game.turnsLeft, curTurnsLeft - 2)

        // test swap one from 0 and 2
        // test swap one from the middle
        rootService.playerActionService.rightShift()

        assertEquals(game.actionsLeft, 1)
        assertEquals(game.currentPlayerIndex, 2)
        assertEquals(game.players[game.currentPlayerIndex % 4].name, "b")
        assertEquals(game.turnsLeft, curTurnsLeft - 2)
    }
}