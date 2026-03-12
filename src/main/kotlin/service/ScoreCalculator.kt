package service

import entity.*

/**
 * calculates end game score
 */
class ScoreCalculator() {

    /**
     * calculates the game score (the places of players)
     * based on players hands
     * 
     * @param playerList - a list of players
     * 
     * includes only non none players
     * 
     * returns a `List<Pair<Int, Player>>` with the players place and
     * players object
     */
    fun getGameScore(playerList: List<Player>): List<Pair<Int, Player>> {
        val readPlayers = playerList.filter {!it.isNone}
        val playerScores = readPlayers.map {
            player -> player to getMaxPlayerCombo(player)
        }

        val sortedScores = playerScores.sortedByDescending {it.second}
        println("-----------------------------")
        val results = mutableListOf<Pair<Int, Player>>()
        var currentPlace = 1
        var prevScore: Int? = null

        for (scoreInfo in sortedScores) {
            val (player, score) = scoreInfo
            println("${player.name} $score")
            if (prevScore != null && score < prevScore) {
                currentPlace++
            }
            results.add(currentPlace to player)
            prevScore = score
        }
        return results
    }

    private fun getMaxPlayerCombo(player: Player): Int {
        val cardsList = player.handCards + player.openCards

        val cardsValuesListInt = cardsList.map { cardValueToInt(it.value) }
        val cardsSuitsListInt = cardsList.map { cardSuitToInt(it.suit) }

        return when {
            hasRoyalFlush(cardsValuesListInt, cardsSuitsListInt) -> 10
            hasStraightFlush(cardsValuesListInt, cardsSuitsListInt) -> 9
            hasFourOfAKind(cardsValuesListInt) -> 8
            hasFullHouse(cardsValuesListInt) -> 7
            hasFlush(cardsSuitsListInt) -> 6
            hasStraight(cardsValuesListInt) -> 5
            hasOneTriple(cardsValuesListInt) -> 4
            hasTwoPair(cardsValuesListInt) -> 3
            hasOnePair(cardsValuesListInt) -> 2
            else -> 0
        }
    }

    private fun hasOnePair(cardsValues: List<Int>): Boolean {
        return cardsValues.groupingBy{it}
            .eachCount()
            .any { it.value >= 2 }
    }

    private fun hasOneTriple(cardsValues: List<Int>): Boolean {
        return cardsValues.groupingBy{it}
            .eachCount()
            .any { it.value >= 3 }
    }

    private fun hasTwoPair(cardsValues: List<Int>): Boolean {
        val counts = cardsValues.groupingBy{it}.eachCount().values
        return counts.count{it >= 2} >= 2
    }

    private fun hasStraight(cardsValues: List<Int>): Boolean {
        val cardsSorted = cardsValues.sorted()
        var hasStraight = true

        for (i in 1..cardsSorted.size - 1) {
            if (cardsSorted[i] != cardsSorted[i - 1] + 1) {
                hasStraight = false
            }
        }
        if (cardsSorted == listOf(0, 1, 2, 3, 12)) {
            hasStraight = true
        }
        return hasStraight
    }

    private fun hasFullHouse(cardsValues: List<Int>): Boolean {
        val counts = cardsValues.groupingBy{it}.eachCount().values
        return counts.contains(2) && counts.contains(3)
    }

    private fun hasFlush(cardSuits: List<Int>): Boolean {
        return cardSuits.groupingBy{it}.eachCount().values.contains(5)
    }

    private fun hasFourOfAKind(cardsValues: List<Int>): Boolean {
        val counts = cardsValues.groupingBy{it}.eachCount().values
        return counts.contains(4)
    }

    private fun hasStraightFlush(cardsValues: List<Int>, cardsSuits: List<Int>): Boolean {
        return hasStraight(cardsValues) && hasFlush(cardsSuits)
    }

    private fun hasRoyalFlush(cardsValue: List<Int>, cardsSuits: List<Int>): Boolean {
        return hasStraightFlush(cardsValue, cardsSuits) &&
                cardsValue.contains(12)
    }

    private fun cardSuitToInt(cardSuit: CardSuit): Int {
        return when (cardSuit) {
            CardSuit.CLUBS -> 0
            CardSuit.DIAMONDS -> 1
            CardSuit.HEARTS -> 2
            CardSuit.SPADES -> 3
        }

    }

    private fun cardValueToInt(cardValue: CardValue): Int {
        return when (cardValue) {
            CardValue.TWO -> 0
            CardValue.THREE -> 1
            CardValue.FOUR -> 2
            CardValue.FIVE -> 3
            CardValue.SIX -> 4
            CardValue.SEVEN -> 5
            CardValue.EIGHT -> 6
            CardValue.NINE -> 7
            CardValue.TEN -> 8
            CardValue.JACK -> 9
            CardValue.QUEEN -> 10
            CardValue.KING -> 11
            CardValue.ACE -> 12
        }
    }
}
