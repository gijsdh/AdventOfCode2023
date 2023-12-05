fun main(args: Array<String>) {
    val input = getResourceAsText("input.txt")

    val seeds = input.lines()[0]
        .split(" ")
        .drop(1)
        .map { it.toLong() }
        .toMutableList()

    println(seeds)
    val mappings = input
        .split("\n\r")            // Split on empty line
        .map { it.split("\r") }   // split on end of line
        .drop(1)
        .map { it.filter { it.isNotEmpty() }.drop(1) } // drop empty and line with text
        .map {
            it.map {
                it.split(" ")     // split 3 numbers and parse to long.
                    .take(3)
                    .map { it.trim().toLong() }
            }
        }

    var locations = mutableListOf<Long>()
    for (seed in seeds) {
        var location = seed
        for (i in 0 until mappings.size) {
            for (j in 0 until mappings[i].size) {
                var values = mappings[i][j]
                val destination = values[0]
                val source = values[1]
                val range = values[2]
                if (source <= location && source + range > location) { // start is inclusive end not %$^$%$&^..
                    location += (destination - source)
                    break
                }
            }
        }
        locations.add(location)
    }

    println(locations.min())

}




