fun main(args: Array<String>) {
    val input = getResourceAsText("input.txt")
    val inputTest = getResourceAsText("testInput.txt")
    val inputLines = input.lines()

    val seeds = input.lines()[0].split(" ").drop(1).map { it.toLong() }.toMutableList()

    println(seeds)
    val map = input
        .split("\n\r")
        .map { it.split("\r")}
        .drop(1)
        .map { it.filter { it.isNotEmpty() }.drop(1) }

//    println(map)

    var locations = mutableListOf<Long>()

    for (seed in seeds) {
        var location = seed
        for (i in 0 until map.size) {
            for (j in 0 until map[i].size) {
                var values = map[i][j].split(" ")
                val destination = values[0].trim().toLong()
                val source = values[1].trim().toLong()
                val range = values[2].trim().toLong()

                println("$seed $source $location")
                if(source <= location && source + range > location) {
                    location += (destination - source)
                    break;
                    println("$seed $location $destination $source $range")
                }
            }
        }
        locations.add(location)
    }

    println(locations)
    println(locations.min())


}




