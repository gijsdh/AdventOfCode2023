import java.lang.Exception

fun main(args: Array<String>) {
    val input = getResourceAsText("input.txt")
    val inputTest = getResourceAsText("testInput.txt")


    val parsed = input.lines().map { it.split("").filter { it.isNotEmpty() }.map { it.trim()}.toMutableList() }.toMutableList()
    printMap(parsed)




    // 0 right, 1 down, 2 left, 3 up
    var positions = mutableSetOf(Triple(0, -1, 0))
    var seen: Array<Array<Boolean>> = Array(parsed.size) { Array<Boolean>(parsed[0].size) { false } }
    seen[0][0] = true

    var index = 0
    var seenAmount = 0
    var lastSeen = 0
    while (true) {
        var newPostions = mutableSetOf<Triple<Int, Int, Int>>()
        for (pos in positions) {
            var newPos = when (pos.third) {
                0 -> Triple(pos.first, pos.second + 1, 0)
                1 -> Triple(pos.first + 1, pos.second, 1)
                2 -> Triple(pos.first, pos.second - 1, 2)
                3 -> Triple(pos.first - 1, pos.second, 3)
                else -> throw Exception("shit")
            }
            if (isValidIndex(newPos.first, newPos.second, parsed.size, parsed[0].size)) {
                val char = parsed[newPos.first][newPos.second]
                if (char == "/") {
                    when (pos.third) {
                        0 -> newPostions.add(Triple(newPos.first, newPos.second, 3))
                        1 -> newPostions.add(Triple(newPos.first, newPos.second, 2))
                        2 -> newPostions.add(Triple(newPos.first, newPos.second, 1))
                        3 -> newPostions.add(Triple(newPos.first, newPos.second, 0))
                    }

                } else if (char == "\\") {
                    when (pos.third) {
                        0 -> newPostions.add(Triple(newPos.first, newPos.second, 1))
                        1 -> newPostions.add(Triple(newPos.first, newPos.second, 0))
                        2 -> newPostions.add(Triple(newPos.first, newPos.second, 3))
                        3 -> newPostions.add(Triple(newPos.first, newPos.second, 2))
                    }
                } else if (char == "|" && newPos.third in listOf(0, 2)) {
                    newPostions.add(Triple(newPos.first, newPos.second, 3))
                    newPostions.add(Triple(newPos.first, newPos.second, 1))
                } else if (char == "-" && newPos.third in listOf(1, 3)) {
                    newPostions.add(Triple(newPos.first, newPos.second, 0))
                    newPostions.add(Triple(newPos.first, newPos.second, 2))
                } else {
                    newPostions.add(Triple(newPos.first, newPos.second, newPos.third))
                }

            }
        }
//        println(newPostions)
        positions = newPostions.toMutableSet()
        for (pos in positions) {
            seen[pos.first][pos.second] = true
        }

        if (positions.size == 0) {
            break
        }
        seenAmount = seen.flatten().count { it }

//        println("$seenAmount $index")
        if (lastSeen == seenAmount) {
            if (index > 100) {
                break
            }
            index++
        }
        lastSeen = seenAmount
    }


    println(seen.flatten().count { it })
}

private fun isValidIndex(i: Int, j: Int, l: Int, k: Int): Boolean {
    if (i < 0 || j < 0 || i >= l || j >= k) return false
    return true
}


private fun printMap(numbers: List<List<String>>) {
    for (el in numbers) {
        println(el.toList())
    }
    println("--------------------------------------------------------------------------")
}

private fun printMap(numbers: Array<Array<Boolean>>) {
    for (el in numbers) {
        println(el.toList())
    }
    println("--------------------------------------------------------------------------")
}