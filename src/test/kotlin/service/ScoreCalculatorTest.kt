package service
import entity.*
import kotlin.test.*

/**
 * test calculating the score
 */
class ScoreCalculatorTest {
    /**
     * tests: pair, triple, two pairs and full house
     */
    @Test
    fun testFirstFourCombos() {
        val playerA = Player( // pair
            "a", false,
            mutableListOf(
                Card(CardValue.TWO, CardSuit.CLUBS),
                Card(CardValue.TWO, CardSuit.SPADES),
            ),
            mutableListOf(
                Card(CardValue.KING, CardSuit.CLUBS),
                Card(CardValue.QUEEN, CardSuit.SPADES),
                Card(CardValue.THREE, CardSuit.CLUBS),
            )
        )
        val playerB = Player(  // triple
            "b", false,
            mutableListOf(
                Card(CardValue.TWO, CardSuit.CLUBS),
                Card(CardValue.TWO, CardSuit.SPADES),
            ),
            mutableListOf(
                Card(CardValue.TWO, CardSuit.DIAMONDS),
                Card(CardValue.QUEEN, CardSuit.SPADES),
                Card(CardValue.THREE, CardSuit.CLUBS),
            )
        )
        val playerC = Player( // two pairs
            "c", false,
            mutableListOf(
                Card(CardValue.TWO, CardSuit.CLUBS),
                Card(CardValue.TWO, CardSuit.SPADES),
            ),
            mutableListOf(
                Card(CardValue.QUEEN, CardSuit.DIAMONDS),
                Card(CardValue.THREE, CardSuit.SPADES),
                Card(CardValue.THREE, CardSuit.CLUBS),
            )
        )
        val playerD = Player(  // full house
            "d", false,
            mutableListOf(
                Card(CardValue.TWO, CardSuit.CLUBS),
                Card(CardValue.TWO, CardSuit.SPADES),
            ),
            mutableListOf(
                Card(CardValue.TWO, CardSuit.DIAMONDS),
                Card(CardValue.THREE, CardSuit.SPADES),
                Card(CardValue.THREE, CardSuit.CLUBS),
            )
        )
        val playerList = listOf(playerA, playerB, playerC, playerD)
        val sc = ScoreCalculator()
        val playerScores = sc.getGameScore(playerList)
        assertEquals(
            playerScores,
            mutableListOf(1 to playerD, 2 to playerB, 3 to playerC, 4 to playerA)
        )
    }

    /**
     * tests: straight, flush, straight flush and four of a kind
     */
    @Test
    fun testSecondFourCombos() {
        // ------------------------ second test
        val playerA = Player(  // straight
                "a", false,
                mutableListOf(
                    Card(CardValue.TWO, CardSuit.CLUBS),
                    Card(CardValue.THREE, CardSuit.SPADES)
                    ),
                mutableListOf(
                    Card(CardValue.FOUR, CardSuit.CLUBS),
                    Card(CardValue.FIVE, CardSuit.SPADES),
                    Card(CardValue.SIX, CardSuit.CLUBS),
                    )
            )
        val playerB = Player( // flush
                "b", false,
                mutableListOf(
                    Card(CardValue.TWO, CardSuit.CLUBS),
                    Card(CardValue.THREE, CardSuit.CLUBS)
                    ),
                mutableListOf(
                    Card(CardValue.FIVE, CardSuit.CLUBS),
                    Card(CardValue.QUEEN, CardSuit.CLUBS),
                    Card(CardValue.TEN, CardSuit.CLUBS),
                    )
                )
        val playerC = Player( // straight flush
                "c", false,
                mutableListOf(
                    Card(CardValue.TWO, CardSuit.CLUBS),
                    Card(CardValue.THREE, CardSuit.CLUBS)
                    ),
                mutableListOf(
                    Card(CardValue.FOUR, CardSuit.CLUBS),
                    Card(CardValue.FIVE, CardSuit.CLUBS),
                    Card(CardValue.SIX, CardSuit.CLUBS),
                    )
            )
        val playerD = Player( // four of a kind
                "d", false,
                mutableListOf(
                    Card(CardValue.TWO, CardSuit.CLUBS),
                    Card(CardValue.TWO, CardSuit.SPADES)
                    ),
                mutableListOf(
                    Card(CardValue.TWO, CardSuit.DIAMONDS),
                    Card(CardValue.TWO, CardSuit.HEARTS),
                    Card(CardValue.THREE, CardSuit.CLUBS),
                    )
                )
        val playerList = listOf(playerA, playerB, playerC, playerD)
        val sc = ScoreCalculator()
        val playerScores = sc.getGameScore(playerList)
        assertEquals(
            playerScores, mutableListOf(1 to playerC, 2 to playerD, 3 to playerB, 4 to playerA)
        )
    }

    /**
     * tests nothing and royal flush + only 2 players
     */
    @Test
    fun testSecondTwoCombos() {
        // ------------------------ second test 

        val playerA = Player(
                "a",
                false,
                mutableListOf( // nothing
                    Card(CardValue.TWO, CardSuit.CLUBS),
                    Card(CardValue.THREE, CardSuit.SPADES)
                    ),
                mutableListOf(
                    Card(CardValue.KING, CardSuit.DIAMONDS),
                    Card(CardValue.FIVE, CardSuit.HEARTS),
                    Card(CardValue.TEN, CardSuit.CLUBS),
                    )
            )
        val playerB = Player(
                "b",
                false,
                mutableListOf( // royal flush
                    Card(CardValue.TEN, CardSuit.CLUBS),
                    Card(CardValue.JACK, CardSuit.CLUBS)
                    ),
                mutableListOf(
                    Card(CardValue.QUEEN, CardSuit.CLUBS),
                    Card(CardValue.KING, CardSuit.CLUBS),
                    Card(CardValue.ACE, CardSuit.CLUBS),
                    )
                )
        
        val playerList = listOf(playerA, playerB)

        val sc = ScoreCalculator()
        val playerScores = sc.getGameScore(playerList)


        // getGameScore(playerList: List<Player>): List<Pair<Int, Player>>
        assertEquals(
            playerScores,
            mutableListOf(
                1 to playerB,
                2 to playerA
            )
        )
    }
    // tests game score + only 2 players
    @Test
    fun testEmptyPlayers() {

        // ------------------------ second test 

        val playerA = Player(
                "a",
                false,
                mutableListOf(
                    Card(CardValue.TEN, CardSuit.DIAMONDS),
                    Card(CardValue.JACK, CardSuit.DIAMONDS)
                    ),
                mutableListOf(
                    Card(CardValue.QUEEN, CardSuit.DIAMONDS),
                    Card(CardValue.KING, CardSuit.DIAMONDS),
                    Card(CardValue.ACE, CardSuit.DIAMONDS),
                    )
            )
        val playerB = Player(
                "b",
                false,
                mutableListOf(
                    Card(CardValue.TEN, CardSuit.CLUBS),
                    Card(CardValue.JACK, CardSuit.CLUBS)
                    ),
                mutableListOf(
                    Card(CardValue.QUEEN, CardSuit.CLUBS),
                    Card(CardValue.KING, CardSuit.CLUBS),
                    Card(CardValue.ACE, CardSuit.CLUBS),
                    )
                )
        
        val playerList = listOf(playerA, playerB)

        val sc = ScoreCalculator()
        val playerScores = sc.getGameScore(playerList)

        assertEquals(
            playerScores,
            mutableListOf(
                1 to playerA,
                1 to playerB
            )
        )


    }
}