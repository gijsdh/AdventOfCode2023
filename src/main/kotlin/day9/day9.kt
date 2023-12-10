fun main(args: Array<String>) {
    val input = getResourceAsText("input.txt")
    val inputTest = getResourceAsText("testInput.txt")

    val parsed = input.lines().map { it.split(" ").filter { it.isNotEmpty() }.map { it.trim().toLong() }}
    println(parsed)

    println(getSolution(parsed,  true))
    println(getSolution(parsed,  false))
}

private fun getSolution(parsed: List<List<Long>>, partOne: Boolean): Long {
    var sum = 0L
    for (line in parsed) {
        val list = mutableListOf(line)
        while (true) {
            list.add(list
                .last()
                .windowed(2)
                .map { dif(partOne, it)}
            )
            if (list.last().all { it == 0L }) break
        }
        sum += list
            .map { if (partOne) it.last() else it.first() }
            .sum()
    }
    return sum
}
private fun dif(partOne: Boolean, it: List<Long>) = if (partOne) it[1] - it[0] else it[0] - it[1]





