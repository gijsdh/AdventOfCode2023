import kotlin.math.max

fun main(args: Array<String>) {
    val input = getResourceAsText("input.txt")
    val inputLines = input.lines().map { it.split(Regex("Game|:|,|;")).map { it.trim() }.filter { it.isNotEmpty() } }
    println(inputLines[0])

    //only 12 red cubes, 13 green cubes, and 14 blue cubes
    var map: MutableMap<String, Int> = mutableMapOf(
        "red" to 12,
        "green" to 13,
        "blue" to 14
    );


    var sum2 = 0L
    var sum1 = 0L
    for (line in inputLines) {
        var minMap: MutableMap<String, Int> = mutableMapOf("red" to 0, "green" to 0, "blue" to 0);
        var add = true
        for (i in 1 until line.size) {
            val split = line[i].split(" ")
            val number = split[0]
            val colour = split[1]

            minMap.merge(colour, number.toInt()) { x, y -> max(x, y) }
            if (map[colour]!! < number.toLong()) add = false

            if (i == line.size - 1) {
                if (add) sum1 += line[0].toInt()
                sum2 += minMap.values.reduce { x, y -> x * y }
            }
        }
    }
    println(sum1)
    println(sum2)
}








