import kotlin.math.max
import kotlin.math.min

fun main(args: Array<String>) {
    val input = getResourceAsText("input.txt")
    val inputTest = getResourceAsText("testInput.txt")
    val inputLines = input.lines()

    val seeds = input.lines()[0]
        .split(" ")
        .drop(1)
        .map { it.toLong() }
        .toMutableList()

    val seedPairs = seeds.windowed(2,2)
        .map { Pair(it[0], it[0] + it[1] -1) }
        .toMutableList() // the range is [) so inclusive start, exclusive end. That why we have the -1.

    println(seedPairs)

    val map = input
        .split("\n\r")
        .map { it.split("\r")}
        .drop(1)
        .map { it.filter { it.isNotEmpty() }.drop(1) }

    var results = mutableListOf<Pair<Long, Long>>()

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

                // we can look at this like line intervals.
                //      line 1  X1-------------------------X2              -> seeds range.
                //      line 2             X1--------------------------X2  -> rules X1 = source, X2 = source + range
                //      = new lines
                //      line 1  X1---------X2
                //      line 2              X1--------------X2
                //      line 3                               X1--------X2
                //      Here line1 and line3 are going to look at the next rule, if they exist of course.
                //      And lin2 is mapped, so we ignore for next rule.
                for (loc in location) {
                    val before = Pair(loc.first, min(source, loc.second))
                    val between : Pair<Long, Long> = Pair(max(loc.first, source), min(loc.second, max))
                    val after = Pair(max(max, loc.first), loc.second)

                    if (before.first < before.second) { // if before segment is not present, start interval > then source || .
                        workSet.add(before)
                    }
                    if(between.first < between.second) {
                        newLocations.add(Pair(between.first + destination - source, between.second + destination - source))
                    }
                    if(after.first < after.second) {
                        workSet.add(after)
                    }
                }
                location = workSet.toMutableSet(); // These are line segments which could not be mapped, so we try mapping them again in new rule. .toMutableSet() -> to copy, Otherwise shit...
            }
            location.addAll(newLocations)  // add all the mapped ones, the leftovers are added at the end of previous loop.
        }
        results.addAll(location)
    }

    println(results.map { it.first }.min())
}




