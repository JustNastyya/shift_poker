package gui

import entity.CardSuit
import entity.CardValue
import tools.aqua.bgw.visual.ImageVisual

private const val CARDS_FILE = "card_deck.png"
private const val IMG_HEIGHT = 200
private const val IMG_WIDTH = 130


/**
 * loads images for cards
 */
class CardImageLoader {

    /**
     * loads front image for
     * @param suit - card suit
     * @param value cards value
     */
    fun frontImageFor(suit: CardSuit, value: CardValue) =
        getImageByCoordinates(value.column, suit.row)

    val backImage: ImageVisual get() = getImageByCoordinates(2, 4)

    private fun getImageByCoordinates (x: Int, y: Int) = ImageVisual(
        CARDS_FILE,
        IMG_WIDTH,
        IMG_HEIGHT,
        x * IMG_WIDTH,
        y * IMG_HEIGHT,
    )
}

private val CardSuit.row get() = when (this) {
    CardSuit.CLUBS -> 0
    CardSuit.DIAMONDS -> 1
    CardSuit.HEARTS -> 2
    CardSuit.SPADES -> 3
}

private val CardValue.column get() = when (this) {
    CardValue.ACE -> 0
    CardValue.TWO -> 1
    CardValue.THREE -> 2
    CardValue.FOUR -> 3
    CardValue.FIVE -> 4
    CardValue.SIX -> 5
    CardValue.SEVEN -> 6
    CardValue.EIGHT -> 7
    CardValue.NINE -> 8
    CardValue.TEN -> 9
    CardValue.JACK -> 10
    CardValue.QUEEN -> 11
    CardValue.KING -> 12
}