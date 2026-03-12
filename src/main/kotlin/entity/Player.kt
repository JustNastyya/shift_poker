package entity

/** class storing important game relevant information about the player with:
 * @param name : string of players name
 * @param isNone : if the player exists (none players still roll around the table)
 * @param handCards : players hidden cards of type `MutableList<Card>`
 * @param openCards : players open cards of type `MutableList<Card>`
 * */
data class Player(
    /** players name*/
    var name: String = "",
    /** if the player exists*/
    var isNone: Boolean = true,
    /** players hidden cards*/
    var handCards: MutableList<Card> = mutableListOf(),
    /** players open cards*/
    var openCards: MutableList<Card> = mutableListOf(),
)