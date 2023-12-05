fun main(args: Array<String>) {
    val input = getResourceAsText("input.txt")
    val inputTest = getResourceAsText("testInput.txt")
    val inputLines = input.lines()

    // Parsing was fun again
    val lines = input.lines()
        .map { it.split(":") }
        .map { it.drop(1) } // drop Game %d
        .flatten()
        .map {
                it.split("|")// split into 2 parts
                    .map {
                        it.split(" ") // split into numbers
                            .filter { it.isNotEmpty() }
                            .map { it.toInt() }
                    }
        }

    val cards = IntArray(lines.size) { 1 }
    var sum = 0
    for ((i, line) in lines.withIndex()) {
        val setOne = line[0].toSet()
        val setTwo = line[1].toSet()

        // intersects; find all which number are present in setOne and setTwo.
        val intersect = setOne.intersect(setTwo)

        //Part 1
        if (intersect.isNotEmpty()) {
            var number = 1
            repeat(intersect.size - 1) { _ -> number *= 2 }
            sum += number
        }

        //Part 2
        for (j in 1 until intersect.size + 1) {
            cards[i + j] += cards[i]
        }
    }

    println(sum)
    println(cards.sum())
}




