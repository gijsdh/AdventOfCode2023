fun main(args: Array<String>) {
    val input = getResourceAsText("input.txt")

    val parsed = input.lines()
        .map {
            it.split(Regex("Distance: |Time: |  "))
                .map { it.trim() }
                .filter { it.isNotEmpty() }

        }

    println(parsed)

    val lines = parsed.map { it.map { it.toLong() } }
    val sumArray = LongArray(lines[0].size) { 0 }

    for ((i, time) in lines[0].withIndex()) {
        val record = lines[1][i]
        sumArray[i] = calculateWaysToRecord(time, record)
    }
    
    //Part one
    println(sumArray.reduce { i, j -> i * j })

    // Parsing part two
    val line = parsed
        .map { it.joinToString("")}
        .map { it.toLong() }

    //Part two
    println(calculateWaysToRecord(line[0], line[1]))
    println(calculateWaysToRecordBinarySearch(line[0], line[1]))

}

private fun calculateWaysToRecord(time: Long, record: Long): Long {
    var sum = 0L
    for (j in 0 until time) {
        val distance = j * (time - j)
        if (distance > record) sum++
    }
    return sum
}

private fun calculateWaysToRecordBinarySearch(time: Long, record: Long): Long {
    val high = findHighOrLow(time, record, { a, b -> a > b }, { a, b -> a < b })
    val low = findHighOrLow(time, record, { a, b -> a < b }, { a, b -> a > b })

    return high - low
}

private fun findHighOrLow(
    time: Long,
    record: Long,
    evalLow: (one: Long, two: Long) -> Boolean,
    evalHigh: (one: Long, two: Long) -> Boolean
): Long {
    var low = 0L
    var high = time
    while (low + 1 < high) {
        val mid = low + (high - low) / 2L
        val distance = mid * (time - mid)
        if (evalLow(distance, record)) {
            low = mid
        } else if (evalHigh(distance,record)) {
            high = mid
        }
    }
    return low
}




