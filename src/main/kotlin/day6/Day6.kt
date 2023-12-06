fun main(args: Array<String>) {
    val input = getResourceAsText("input.txt")

    val parsed = input.lines()
        .map {
            it.split(Regex("Distance: |Time: |  "))
                .map { it.trim() }
                .filter { it.isNotEmpty() }}

    println(parsed)


    val lines = parsed.map { it.map { it.toLong() } }
    val sumArray = LongArray(lines[0].size) { 0 }

    for ((i, time) in lines[0].withIndex()) {
        val record = lines[1][i]
        sumArray[i] = calculateWaysToRecord(time, record)
    }
    
    //Part one
    println(sumArray.reduce{i, j -> i*j})

    // Parsing part two
    val line = parsed.map { it.reduce { i, j -> i + j } }.map { it.toLong() }

    //Part two
    println(calculateWaysToRecord(line[0], line[1]))
}

private fun calculateWaysToRecord(time: Long, record: Long): Long {
    var sum = 0L
    for (j in 0 until time) {
        val distance = j * (time - j)
        if (distance > record) sum++
    }
    return sum
}




