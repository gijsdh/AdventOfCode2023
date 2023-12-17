import java.lang.Exception

fun main(args: Array<String>) {
    val input = getResourceAsText("input.txt")
    val inputTest = getResourceAsText("testInput.txt")

    val parsed = input.lines()
        .map {
            it.split("")
                .filter { it.isNotEmpty() }
                .map { it.trim() }
        }

    // triple (y, x, direction)
    // direction -> 0 = right, 1 = down, 2 = left, 3 = up
    // We start from outside of the grid, for part one [0][-1].
    val startPosistions = mutableSetOf<Triple<Int, Int, Int>>()
    // The sides
    for (i in 0 until parsed.size ) {
        startPosistions.add(Triple(i, -1, 0))
        startPosistions.add(Triple(i, parsed[0].size, 2))
    }
    // The bottom and top
    for (i in 0 until parsed[0].size ) {
        startPosistions.add(Triple(-1, i, 1))
        startPosistions.add(Triple(parsed.size, i, 3))
    }


    println("Answer B: ${ getMax(startPosistions, parsed)}")

    println("Answer A: ${getMax(mutableSetOf(Triple(0,-1,0)), parsed)}")

}

private fun getMax(
    startPosistions: MutableSet<Triple<Int, Int, Int>>,
    parsed: List<List<String>>
) : Int{
    var max = 0
    for ((k, start) in startPosistions.withIndex()) {
        var positions = mutableSetOf(start)
        val seen: Array<Array<Boolean>> = Array(parsed.size) { Array(parsed[0].size) { false } }

        var index = 0
        var seenAmount = 0
        var lastSeen = 0
        while (true) {
            positions = calculateNewPostions(positions, parsed)

            for (pos in positions) {
                seen[pos.first][pos.second] = true
            }

            // Break out when there are not more
            if (positions.size == 0) {
                println("No more light")
                break
            }
            seenAmount = seen.flatten().count { it }

            // In the end we end up with rays in a path they are have already been in.
            // We hope that there are no new ray paths after 10 iterations, then we stop
            if (lastSeen == seenAmount) {
                if (index > 10) {
                    println("stable position")
                    break
                }
                index++
            }
            lastSeen = seenAmount
        }
        println("${startPosistions.size} $k")
        if (seenAmount > max) {
            max = seenAmount
        }

    }
    return max
}

private fun calculateNewPostions(
    positions: MutableSet<Triple<Int, Int, Int>>,
    parsed: List<List<String>>
): MutableSet<Triple<Int, Int, Int>> {
    val newPositions = mutableSetOf<Triple<Int, Int, Int>>()

    for (pos in positions) {
        var newPos = newPosition(pos)
        if (isValidIndex(newPos.first, newPos.second, parsed.size, parsed[0].size)) {
            val char = parsed[newPos.first][newPos.second]
            if (char == "/") {
                val direction = when (newPos.third) {
                    0 -> 3
                    1 -> 2
                    2 -> 1
                    3 -> 0
                    else -> throw Exception("shit")
                }
                newPositions.add(Triple(newPos.first, newPos.second, direction))
            } else if (char == "\\") {
                val direction =  when (newPos.third) {
                    0 -> 1
                    1 -> 0
                    2 -> 3
                    3 -> 2
                    else -> throw Exception("shit")
                }
                newPositions.add(Triple(newPos.first, newPos.second, direction))
            } else if (char == "|" && newPos.third in listOf(0, 2)) {
                newPositions.add(Triple(newPos.first, newPos.second, 3))
                newPositions.add(Triple(newPos.first, newPos.second, 1))
            } else if (char == "-" && newPos.third in listOf(1, 3)) {
                newPositions.add(Triple(newPos.first, newPos.second, 0))
                newPositions.add(Triple(newPos.first, newPos.second, 2))
            } else {
                newPositions.add(Triple(newPos.first, newPos.second, newPos.third))
            }
        }
    }
    return newPositions
}

private fun newPosition(pos: Triple<Int, Int, Int>) =
    when (pos.third) {
        0 -> Triple(pos.first, pos.second + 1, 0)
        1 -> Triple(pos.first + 1, pos.second, 1)
        2 -> Triple(pos.first, pos.second - 1, 2)
        3 -> Triple(pos.first - 1, pos.second, 3)
        else -> throw Exception("shit")
    }

private fun isValidIndex(i: Int, j: Int, l: Int, k: Int): Boolean = !(i < 0 || j < 0 || i >= l || j >= k)

