import java.lang.Exception

fun main(args: Array<String>) {
    val input = getResourceAsText("input.txt")
    val inputTest = getResourceAsText("testInput.txt")
    val parsed = input.lines().map { it.split(" ").filter { it.isNotEmpty() } }

    // Good to know, standard sorting is from low to high in kotlin, so 2,5,3 -> 2,3,5
    // Under the hood, it just looks at char.code.
    val map = mapOf(
        "A" to Char(13),
        "K" to Char(12),
        "Q" to Char(11),
        "J" to Char(10),
        "T" to Char(9),
        "9" to Char(8),
        "8" to Char(7),
        "7" to Char(6),
        "6" to Char(5),
        "5" to Char(4),
        "4" to Char(3),
        "3" to Char(2),
        "2" to Char(1)
    ).toMutableMap()
    map.replace("J", Char(0))


    val sorted = parsed.sortedWith(
        compareBy<List<String>> { strengthHand(it) }
            .then(compareBy {
                it[0].map { map[it.toString()] }
                     .joinToString("")
                // We transform individual cards to character with code going from 1 -> 13.
                // Then we let the standard sorting take place.
            })
    )

    var sum = 0L
    for ((i, line) in sorted.withIndex()) {
        sum += line[1].toLong() * (i+1)
    }
    println(sum)
    println(sorted)
}

private fun strengthHand(line: List<String>): Int {
    val occurrencesMap = mutableMapOf<Char, Int>()
    val replace = line[0].replace("J", "")
    val count = 5 - replace.length

    for (char in replace) {
        occurrencesMap.merge(char, 1, { i, j -> i + j })
    }

    val strength = occurrencesMap.values.sorted().toMutableList()
    if (count == 5) strength.add(count) else strength[strength.size - 1] = strength.last() + count


    return when (strength) {
        listOf(5) -> 9
        listOf(1, 4) -> 8
        listOf(2, 3) -> 7
        listOf(1, 1, 3) -> 6
        listOf(1, 2, 2) -> 5
        listOf(1, 1, 1, 2) -> 4
        listOf(1, 1, 1, 1, 1) -> 3
        else -> {
            println(strength); throw Exception("Shit")
        }
    }
}




