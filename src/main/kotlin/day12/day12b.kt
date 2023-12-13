fun main(args: Array<String>) {
    val input = getResourceAsText("input.txt")
    val inputTest = getResourceAsText("testInput.txt")

    val parsed = input.lines()
        .map { it.split(" ")
            .map { it.split(",")
            .filter { it.isNotEmpty() } }
        }

    var sum = 0L
    for (line in parsed) {
        val value = List(5) { line[0][0] }.joinToString("?").split("").filter { it.isNotEmpty() }
        val chunks = List(5) { line[1].map { it.toInt() } }.flatten()

        val calculate = calculate(value, chunks, 0, 0, 0)
        memoization.clear()
        sum += calculate
    }

    println(sum)
}

private val memoization = mutableMapOf<Triple<Int, Int, Int>, Long>()

fun calculate(value: List<String>, chunks: List<Int>, index: Int, indexChunks: Int, count: Int): Long {
    if(memoization.contains(Triple(index, indexChunks, count))) return memoization[Triple(index, indexChunks, count)]!!
    if (index == value.size) {
        if (chunks.size - 1 == indexChunks && chunks[indexChunks] == count) return 1
        if (chunks.size == indexChunks && count == 0) return 1
        return 0
    }

    var sum = 0L
    if(value[index] in listOf(".", "?")) {
        if (indexChunks < chunks.size && chunks[indexChunks] == count) sum += calculate(value, chunks, index + 1, indexChunks + 1, 0)
        if(count == 0) sum += calculate(value, chunks, index + 1, indexChunks, 0);
    }
    if(value[index] in listOf("#", "?")) sum += calculate(value, chunks, index + 1 , indexChunks, count + 1)

    memoization[Triple(index, indexChunks, count)] = sum
    return sum
}







