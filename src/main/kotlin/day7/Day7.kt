import java.lang.Exception

fun main(args: Array<String>) {
    val input = getResourceAsText("input.txt")
    val inputTest = getResourceAsText("testInput.txt")

    val parsed = input.lines().map { it.split(" ").filter { it.isNotEmpty() } }

    println(parsed)

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
        "2" to "a")


    var sorted = parsed.sortedWith(
        compareBy<List<String>> {
            var a = strengthHand(it)

            a
        }.then(
            compareBy<List<String>> {
            it[0].split("").filter { it.isNotEmpty() }.map { map[it] }.joinToString("")
        }
        )
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
    for (char in line[0]) {
        occurrencesMap.merge(char, 1, { i, j -> i + j })
    }
    var strength = occurrencesMap.values.toMutableList().sorted()
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




