fun main(args: Array<String>) {
    val input = getResourceAsText("input.txt")
    val inputTest = getResourceAsText("testInput.txt")

    val parsed = input.lines().map { it.split("").filter { it.isNotEmpty() }.map { it.trim() }.toMutableList() }
        .toMutableList()

    printMap(parsed)
    var count = parsed.flatten().count { it == "O" }
    println(count)

    var index = 0L;

    // We are storing the whole map as a key, quit a lot of data
    var map: MutableMap<List<List<String>>, Long> = mutableMapOf()
    var matched = false
    while (index < 1000000000) {
        for ((i, row) in parsed.withIndex()) {
            for ((j, char) in row.withIndex()) {
                if (char == "O") {
                    for (k in i - 1 downTo 0) {
                        if (parsed[k][j] == "O" || parsed[k][j] == "#") {
                            parsed[k + 1][j] = "O"
                            if (k + 1 != i) parsed[i][j] = "."
                            break
                        }
                        if (k == 0) {
                            parsed[k][j] = "O"
                            if (k != i) parsed[i][j] = "."
                        }
                    }
                }
            }
        }

        // Probably better to rotate, this was a bit mistake intensive
        for ((i, row) in parsed.withIndex()) {
            for ((j, char) in row.withIndex()) {
                if (char == "O") {
                    for (k in j - 1 downTo 0) {
                        if (parsed[i][k] == "O" || parsed[i][k] == "#") {
                            parsed[i][k + 1] = "O"
                            if (k + 1 != j) parsed[i][j] = "."
                            break
                        }
                        if (k == 0) {
                            parsed[i][k] = "O"
                            if (k != j) parsed[i][j] = "."
                        }
                    }
                }
            }
        }

        for (i in parsed.size - 1 downTo 0) {
            for (j in 0 until parsed[0].size) {
                if (parsed[i][j] == "O") {
                    for (k in i + 1 until parsed.size) {
                        if (parsed[k][j] == "O" || parsed[k][j] == "#") {
                            parsed[k - 1][j] = "O"
                            if (k - 1 != i) parsed[i][j] = "."
                            break
                        }
                        if (k == parsed.size - 1) {
                            parsed[k][j] = "O"
                            if (k != i) parsed[i][j] = "."
                        }
                    }
                }
            }
        }

        for (i in parsed.size - 1 downTo 0) {
            for (j in parsed[0].size - 1 downTo 0) {

                if (parsed[i][j] == "O") {
                    for (k in j + 1 until parsed[0].size) {
                        if (parsed[i][k] == "O" || parsed[i][k] == "#") {

                            parsed[i][k - 1] = "O"
                            if (k - 1 != j) parsed[i][j] = "."
                            break
                        }
                        if (k == parsed[0].size - 1) {
                            parsed[i][k] = "O"
                            if (k != j) parsed[i][j] = "."
                        }
                    }
                }
            }
        }
        println(index)
        println(parsed.flatten().count { it == "O" })
        printMap(parsed)

        // The pattern will repeat it self after x cycles.
        // We use this to extrapolate the result to results. So we don't have to go through 10^9 cycles.
        if (!matched && map.contains(parsed)) {
            val newIndex = map[parsed]!!
            val delta = index - newIndex
            val amt = (1000000000L - index) / delta

            index += amt * delta
            println(index)
            matched = true
        }

        map.put(parsed, index)
        index++
    }

    println(parsed.flatten().count { it == "O" })
    printMap(parsed)

    var sum = 0
    for ((i, row) in parsed.withIndex()) {
        for ((j, char) in row.withIndex()) {
            if (char == "O") {
                sum += parsed.size - i
            }
        }
    }

    println(sum)


}

private fun printMap(numbers: List<List<String>>) {
    for (el in numbers) {
        println(el.toList())
    }
    println("--------------------------------------------------------------------------")
}