import java.lang.Exception

fun main(args: Array<String>) {
    val input = getResourceAsText("input.txt")
    val inputTest = getResourceAsText("testInput.txt")
    val parsed = input.lines().map { it.split(" ").filter { it.isNotEmpty() } }

    val map = mapOf(
        "A" to "m",
        "K" to "l",
        "Q" to "k",
        "J" to "j",
        "T" to "i",
        "9" to "h",
        "8" to "g",
        "7" to "f",
        "6" to "e",
        "5" to "d",
        "4" to "c",
        "3" to "b",
        "2" to "a").toMutableMap()

    // Replace J to bottom of the list.
    map.replace("J", Char(('a'.code-1)).toString())

    val sorted = parsed.sortedWith(
        compareBy<List<String>> { strengthHand(it) }
            .then(compareBy { it[0]
                .map { map[it.toString()] }
                .joinToString("")
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

    var strength = occurrencesMap.values.sorted().toMutableList()
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




