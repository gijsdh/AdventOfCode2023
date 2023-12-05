import kotlin.math.max
import kotlin.math.min

fun main(args: Array<String>) {
    val input = getResourceAsText("input.txt")
    val inputTest = getResourceAsText("testInput.txt")
    val inputLines = input.lines()

    val seeds = input.lines()[0].split(" ").drop(1).map { it.toLong() }.toMutableList()

    val seedPairs = seeds.windowed(2,2).map { Pair(it[0], it[0] + it[1] -1) }.toMutableList()
    println(seedPairs)


    println(seeds)
    val map = input
        .split("\n\r")
        .map { it.split("\r")}
        .drop(1)
        .map { it.filter { it.isNotEmpty() }.drop(1) }

    var locations = mutableListOf<Pair<Long, Long>>()

    for (seed in seedPairs) {

        var location = mutableSetOf(seed)

        for (i in 0 until map.size) {
            var newLocations = mutableSetOf<Pair<Long, Long>>()
            for (j in 0 until map[i].size) {
                var values = map[i][j].split(" ")
                val destination = values[0].trim().toLong()
                val source = values[1].trim().toLong()
                val range = values[2].trim().toLong()
                val max = source + range

                var workSet = mutableSetOf<Pair<Long, Long>>()
                for (loc in location) {
                    val before = Pair(loc.first, min(source, loc.second))
                    val between : Pair<Long, Long> = Pair(max(loc.first, source), min(loc.second, max))
                    val after = Pair(max(max, loc.first), loc.second)

                    if (before.first < before.second) {
                        workSet.add(before)
                    }
                    if(between.first < between.second) {
                        newLocations.add(Pair(between.first + destination - source, between.second + destination - source))
                    }
                    if(after.first < after.second) {
                        workSet.add(after)
                    }
                }
                location = workSet.toMutableSet();
            }
            location.addAll(newLocations)
        }
        locations.addAll(location)
    }

    println(locations.map { it.first }.min())
//    println(locations.min())


}




