import java.util.*

fun main(args: Array<String>) {
    val input = getResourceAsText("input.txt")
    val testInput = getResourceAsText("testInput.txt")

    val parse =  input.split("\n\r\n")
        .map { it.split("\r")
            .drop(1)
            .filter { it.isNotEmpty() }
            .map { it.trim() }
            .map { it.toLong() }
        }

    val deckA = LinkedList(parse[0])
    val deckB = LinkedList(parse[1])

    while (deckB.size > 0 && deckA.size > 0) {
        println(deckA)
        println(deckB)
        val cardA = deckA.remove()
        val cardB = deckB.remove()

        if(cardA > cardB) {
            deckA.add(cardA)
            deckA.add(cardB)
        } else {
            deckB.add(cardB)
            deckB.add(cardA)
        }
    }

    println(deckB)
    var sum =0L
    if(deckB.size>0) {
        for ((i, card) in deckB.reversed().withIndex()) {
            sum += (i +1) *card
        }
    } else {
        for ((i, card) in deckA.reversed().withIndex()) {
            sum += (i +1) *card
        }
    }

    println(sum)

}




