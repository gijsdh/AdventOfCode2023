fun main(args: Array<String>) {
    val input = getResourceAsText("input.txt")
    val inputTest = getResourceAsText("testInput.txt")

    val parsed = input.lines().map { it.split("").filter { it.isNotEmpty() }.map { it.trim()}.toMutableList() }.toMutableList()

    printMap(parsed)
    var count = parsed.flatten().count { it == "O" }
    println(count)

    for ((i, row) in parsed.withIndex()) {
        for ((j, char) in row.withIndex()) {
            if (char == "O") {
                for (k in i - 1 downTo 0) {
//                    println("$i $j $k")
                    if (parsed[k][j] == "O" || parsed[k][j] == "#") {
//                        println("here")
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