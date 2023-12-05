fun main(args: Array<String>) {
    val input = getResourceAsText("input.txt")
    val value = input.lines().map { it.split("x").map { it.toLong() } }
    println(value)

    var sum = 0L
    for (line in value) {
        sum += 2 * line[0] * line[1] + 2 * line[1] * line[2] + 2 * line[0] * line[2]
        sum += Math.min(Math.min(line[0] * line[1], line[1] * line[2]), line[0] * line[2])
    }
    println(sum)

    var sum2 = 0L
    for (line in value) {
        sum2 += line[0] * line[1] * line[2]
        val sortedLine = line.sorted()
        sum2 += sortedLine[0] * 2 + sortedLine[1] * 2
    }

    println(sum2)
}







