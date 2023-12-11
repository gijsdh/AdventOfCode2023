import java.lang.Math.abs

fun main(args: Array<String>) {
    val input = getResourceAsText("input.txt")
    val inputTest = getResourceAsText("testInput.txt")

    val parsed = input.lines().map { it.split("").filter { it.isNotEmpty() }.map { it.trim()} }
    val list = mutableListOf<Pair<Int, Int>>()

    for ((i, row) in parsed.withIndex()) {
        for ((j, char) in row.withIndex()) {
            if (char == "#") list.add(Pair(i, j))
        }
    }

    // find rows and columns with that need to be expanded
    var rows = parsed.indices.toMutableSet()
    var collums = parsed[0].indices.toMutableSet()
    for (value in list) {
        rows.remove(value.first)
        collums.remove(value.second)
    }

    println(calculateDistances(list, rows, collums, 2))
    println(calculateDistances(list, rows, collums, 1000000))


}

private fun calculateDistances(list: MutableList<Pair<Int, Int>>, rows: MutableSet<Int>, colums: MutableSet<Int>, expansion: Long): Long {
    var sum = 0L
    for ((i, galaxy) in list.withIndex()) {
        for (j in i + 1 until list.size) {
            val twoX = list[j].first
            val twoY = list[j].second

            var rangeRows = (if (twoX > galaxy.first) galaxy.first until twoX else twoX until galaxy.first).toMutableSet()
            var rangeCollums = (if (twoY > galaxy.second) galaxy.second until twoY else twoY until galaxy.second).toMutableSet()
            sum += (expansion - 1) * rows.intersect(rangeRows).size
            sum += (expansion - 1) * colums.intersect(rangeCollums).size

            var manhattan = abs(galaxy.first - twoX) + abs(galaxy.second - twoY)
            sum += manhattan
        }
    }
    return sum
}






