import kotlin.math.max
import kotlin.math.min
import kotlin.math.abs

fun main(args: Array<String>) {
    val input = getResourceAsText("input.txt")
    val inputTest = getResourceAsText("testInput.txt")

    val parsed = input.lines().map { it.split("").filter { it.isNotEmpty() }.map { it.trim()} }
    val galaxies = mutableListOf<Pair<Int, Int>>()

    for ((i, row) in parsed.withIndex()) {
        for ((j, char) in row.withIndex()) {
            if (char == "#") galaxies.add(Pair(i, j))
        }
    }

    // find rows and columns with that need to be expanded
    val rows = parsed.indices.toMutableSet()
    val columns = parsed[0].indices.toMutableSet()
    for (galaxy in galaxies) {
        rows.remove(galaxy.first)
        columns.remove(galaxy.second)
    }

    println(calculateDistances(galaxies, rows, columns, 2))
    println(calculateDistances(galaxies, rows, columns, 1000000))
}

private fun calculateDistances(list: MutableList<Pair<Int, Int>>, rows: MutableSet<Int>, columns: MutableSet<Int>, expansion: Long): Long {
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
            sum += (expansion - 1) * columns.intersect(rangeColumns).size
            sum += abs(galaxyX - compareX) + abs(galaxyY - compareY)
        }
    }
    return sum
}






