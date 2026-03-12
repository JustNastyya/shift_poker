package entity
import kotlin.random.Random


/** Card Stack used for draw cards and for discard pile
 * stores a list of cards
 * @param allCards of type `List<Card>`. the list of cards
 * */
class CardStack(val allCards: List<Card>) {

    private val cards: ArrayDeque<Card> = ArrayDeque(allCards)

    /** returns Int amount of cards in the stack*/
    val size: Int get() = cards.size

    /** returns a boolean value corresponding to emptiness*/
    val empty: Boolean get() = cards.isEmpty()

    /** shuffle cards in the stack */
    fun shuffle() {
        cards.shuffle(Random)
    }
    /** pop one card from the top of the stack*/
    fun draw(): Card {
        return cards.removeFirst()
    }
    /** draw (pop) "amount" of cards from the top of the stack*/
    fun drawMany(amount: Int = 1): MutableList<Card> {
        val drawnCards = mutableListOf<Card>()
        repeat(amount) {
            drawnCards.add(cards.removeFirst())
        }
        return drawnCards
    }
    /** puts a card on top of the stack*/
    fun putOnTop(card: Card) {
        cards.addFirst(card)
    }
    /** get list of all cards in a stack*/
    fun peekAll(): List<Card> = cards.toList()
}
