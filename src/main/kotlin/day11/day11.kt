import kotlin.math.max
import kotlin.math.min
import kotlin.math.abs

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
    var colums = parsed[0].indices.toMutableSet()
    for (value in list) {
        rows.remove(value.first)
        colums.remove(value.second)
    }

    println(calculateDistances(list, rows, colums, 2))
    println(calculateDistances(list, rows, colums, 1000000))
}

private fun calculateDistances(list: MutableList<Pair<Int, Int>>, rows: MutableSet<Int>, colums: MutableSet<Int>, expansion: Long): Long {
    var sum = 0L
    for ((i, galaxy) in list.withIndex()) {
        for (j in i + 1 until list.size) {
            val compareX = list[j].first
            val compareY = list[j].second

            val galaxyX = galaxy.first
            val galaxyY = galaxy.second

            val rangeRows = (min(compareX, galaxyX) until max(compareX, galaxyX)).toMutableSet()
            val rangeColumns = (min(compareY, galaxyY) until max(compareY, galaxyY)).toMutableSet()

            sum += (expansion - 1) * rows.intersect(rangeRows).size
            sum += (expansion - 1) * colums.intersect(rangeColumns).size
            sum += abs(galaxyX - compareX) + abs(galaxyY - compareY)
        }
    }
    return sum
}






