package service
import entity.CardSuit
import entity.CardValue
import entity.Card
import entity.CardStack
import kotlin.test.*
import org.junit.jupiter.api.Assertions.assertThrows

/**
 * tests Game service. uses root service for lateinit
 */
class GameServiceTest {
    /**
     * The [RootService] is initialized in the [setUpGame] function
     * hence it is a late-initialized property.
     */
    private lateinit var rootService: RootService
    private lateinit var cards: List<Card>

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
        val testRefreshable = TestRefreshable()
        rootService.addRefreshable(testRefreshable)

        cards = listOf(
            Card(CardValue.QUEEN, CardSuit.CLUBS),
            Card(CardValue.TEN, CardSuit.SPADES),
            Card(CardValue.SEVEN, CardSuit.DIAMONDS),
            Card(CardValue.EIGHT, CardSuit.CLUBS),
            Card(CardValue.NINE, CardSuit.CLUBS),
            Card(CardValue.KING, CardSuit.HEARTS),
            Card(CardValue.QUEEN, CardSuit.DIAMONDS),
            Card(CardValue.QUEEN, CardSuit.SPADES),
            Card(CardValue.JACK, CardSuit.DIAMONDS),
            Card(CardValue.SEVEN, CardSuit.SPADES),
            Card(CardValue.KING, CardSuit.DIAMONDS),
            Card(CardValue.NINE, CardSuit.DIAMONDS),
            Card(CardValue.EIGHT, CardSuit.SPADES),
            Card(CardValue.TEN, CardSuit.HEARTS),
            Card(CardValue.EIGHT, CardSuit.HEARTS),
            Card(CardValue.JACK, CardSuit.CLUBS),
            Card(CardValue.ACE, CardSuit.HEARTS),
            Card(CardValue.NINE, CardSuit.SPADES),
            Card(CardValue.ACE, CardSuit.CLUBS),
            Card(CardValue.JACK, CardSuit.SPADES),
            Card(CardValue.SEVEN, CardSuit.HEARTS),
            Card(CardValue.SEVEN, CardSuit.CLUBS),
            Card(CardValue.KING, CardSuit.CLUBS),
            Card(CardValue.EIGHT, CardSuit.DIAMONDS),
            Card(CardValue.TEN, CardSuit.CLUBS),
            Card(CardValue.TEN, CardSuit.DIAMONDS),
            Card(CardValue.JACK, CardSuit.HEARTS),
            Card(CardValue.KING, CardSuit.SPADES),
            Card(CardValue.ACE, CardSuit.DIAMONDS),
            Card(CardValue.QUEEN, CardSuit.HEARTS),
            Card(CardValue.ACE, CardSuit.SPADES),
            Card(CardValue.NINE, CardSuit.HEARTS),
            Card(CardValue.SIX, CardSuit.HEARTS),
            Card(CardValue.SIX, CardSuit.SPADES),
            Card(CardValue.SIX, CardSuit.DIAMONDS),
            Card(CardValue.SIX, CardSuit.CLUBS),
            Card(CardValue.FIVE, CardSuit.HEARTS),
            Card(CardValue.FIVE, CardSuit.SPADES),
            Card(CardValue.FIVE, CardSuit.DIAMONDS),
            Card(CardValue.FIVE, CardSuit.CLUBS),
            Card(CardValue.FOUR, CardSuit.HEARTS),
            Card(CardValue.FOUR, CardSuit.SPADES),
            Card(CardValue.FOUR, CardSuit.DIAMONDS),
            Card(CardValue.FOUR, CardSuit.CLUBS),
            Card(CardValue.THREE, CardSuit.HEARTS),
            Card(CardValue.THREE, CardSuit.SPADES),
            Card(CardValue.THREE, CardSuit.DIAMONDS),
            Card(CardValue.THREE, CardSuit.CLUBS),
            Card(CardValue.TWO, CardSuit.HEARTS),
            Card(CardValue.TWO, CardSuit.SPADES),
            Card(CardValue.TWO, CardSuit.DIAMONDS),
            Card(CardValue.TWO, CardSuit.CLUBS)
            )
        println(rootService)
    }


    /**
     * tests for getting game before a game was initialized
     * 
     */

    @Test
    fun testGetGame() {
        assertThrows(IllegalStateException::class.java) {
            rootService.getCurrentGame()
        }
    }

    /**
     * tests the first setup of the game, including:
     * - gamers order (and number)
     * - exceptions for players order and number of rounds
     * - isNone property of the players
     * - sizes of cards everywhere
     * - if all the cards make up a deck
     */
    @Test
    fun testStartGame() {
        val testRefreshable = TestRefreshable()
        rootService.addRefreshable((testRefreshable))

        rootService.gameService.startGame(listOf("a", "b", "", ""), 2)
        val game = rootService.getCurrentGame()

        assertTrue(testRefreshable.refreshAfterStartGameCalled)
        testRefreshable.reset()

        assertEquals(game.players[0].name, "a")
        assertEquals(game.players[1].name, "")
        assertEquals(game.players[2].name, "b")
        assertEquals(game.players[3].name, "")


        assertEquals(game.players[0].isNone, false)
        assertEquals(game.players[1].isNone, true)
        assertEquals(game.players[2].isNone, false)
        assertEquals(game.players[3].isNone, true)

        assertEquals(game.players[0].openCards.size, 3)
        assertEquals(game.players[0].handCards.size, 2)
        assertEquals(game.players[1].openCards.size, 0)
        assertEquals(game.players[1].handCards.size, 0)
        assertEquals(game.players[2].openCards.size, 3)
        assertEquals(game.players[2].handCards.size, 2)
        assertEquals(game.players[3].openCards.size, 0)
        assertEquals(game.players[3].handCards.size, 0)

        assertEquals(game.rounds, 2)
        assertEquals(game.actionsLeft, 2)
        assertEquals(game.turnsLeft, 8)
        assertContains(listOf(0, 2), game.currentPlayerIndex)

        val allCards = game.drawPile.peekAll() + game.flop +
                game.players[0].openCards + game.players[0].handCards +
                game.players[2].openCards + game.players[2].handCards

        assertEquals(52, allCards.toSet().size)

        assertTrue(allCards.containsAll(cards))
        assertTrue(cards.containsAll(allCards))


        assertThrows(IllegalArgumentException::class.java) {
            rootService.gameService.startGame(listOf("", "b", "a", ""), 3)
        }
        assertThrows(IllegalArgumentException::class.java) {
            rootService.gameService.startGame(listOf("a", "", "b", ""), 3)
        }
        assertThrows(IllegalArgumentException::class.java) {
            rootService.gameService.startGame(listOf("a", "b", "", ""), 1)
        }
        assertThrows(IllegalArgumentException::class.java) {
            rootService.gameService.startGame(listOf("c", "b", "a", ""), 100)
        }

    }

    /**
     * tests the hole players rotation starting from the first move
     * and untill calling score
     */
    @Test
    fun testNextPlayer() {
        rootService.gameService.startGame(listOf("a", "b", "", ""), 2)
        val game = rootService.getCurrentGame()
        val testRefreshable = TestRefreshable()
        rootService.addRefreshable((testRefreshable))
        game.currentPlayerIndex = 0

        // round 1 player 1
        rootService.gameService.nextAction()
        assertEquals(game.actionsLeft, 1)
        assertEquals(game.currentPlayerIndex, 0)
        assertEquals(game.turnsLeft, 8)

        rootService.gameService.nextAction()
        // refreshAfterTurnEnd
        assertTrue(testRefreshable.refreshAfterTurnEndCalled)
        testRefreshable.reset()
        // round 1 player 2
        assertEquals(game.actionsLeft, 2)
        assertEquals(game.currentPlayerIndex, 2)
        assertEquals(game.turnsLeft, 6)

        rootService.gameService.nextAction()
        assertEquals(game.actionsLeft, 1)
        assertEquals(game.currentPlayerIndex, 2)
        assertEquals(game.turnsLeft, 6)

        rootService.gameService.nextAction()
        // refreshAfterTurnEnd
        assertTrue(testRefreshable.refreshAfterTurnEndCalled)
        testRefreshable.reset()
        // round 2 player 1
        assertEquals(game.actionsLeft, 2)
        assertEquals(game.currentPlayerIndex, 4)
        assertEquals(game.turnsLeft, 4)

        rootService.gameService.nextAction()
        assertEquals(game.actionsLeft, 1)
        assertEquals(game.currentPlayerIndex, 4)
        assertEquals(game.turnsLeft, 4)

        rootService.gameService.nextAction()
        // refreshAfterTurnEnd
        assertTrue(testRefreshable.refreshAfterTurnEndCalled)
        testRefreshable.reset()
        // round 2 player 2
        assertEquals(game.actionsLeft, 2)
        assertEquals(game.currentPlayerIndex, 6)
        assertEquals(game.turnsLeft, 2)

        rootService.gameService.nextAction()
        assertEquals(game.actionsLeft, 1)
        assertEquals(game.currentPlayerIndex, 6)
        assertEquals(game.turnsLeft, 2)

        rootService.gameService.nextAction()
        // endgame
        assertEquals(game.actionsLeft, 2)
        assertEquals(game.currentPlayerIndex, 8)
        assertEquals(game.turnsLeft, 0)

        assertTrue(testRefreshable.refreshAfterScoreCalled)
        testRefreshable.reset()
    }

    /**
     * tests if the refilling is right
     */
    @Test
    fun testRefillDrawStack() {
        rootService.gameService.startGame(listOf("a", "b", "", ""), 2)
        val g = rootService.getCurrentGame()
        val cardList = mutableListOf(
            Card(CardValue.TWO, CardSuit.HEARTS),
            Card(CardValue.TWO, CardSuit.SPADES),
            Card(CardValue.TWO, CardSuit.DIAMONDS),
            Card(CardValue.TWO, CardSuit.CLUBS)
        )
        g.discardPile = CardStack(cardList)
        g.drawPile = CardStack(mutableListOf())

        rootService.gameService.refillDrawStack()

        assertTrue(cardList.containsAll(g.drawPile.peekAll()))
        assertTrue(g.drawPile.peekAll().containsAll(cardList))

        assertEquals(g.discardPile.peekAll().size, 0)

    }
}