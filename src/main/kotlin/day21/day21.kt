fun main(args: Array<String>) {
    val input = getResourceAsText("input.txt")
    val inputTest = getResourceAsText("testInput.txt")

    val parsed = input.lines()
        .map {
            it.split("")
                .filter {
                    it.isNotEmpty()
                }
        }
    println(parsed)

    var set = mutableSetOf<Pair<Int, Int>>()

    for (i in parsed.indices) {
        for (j in parsed[0].indices) {
            if (parsed[i][j] == "S") set.add(Pair(i, j))
        }
    }

    println(set)

    var index = 0
    while (index < 64) {

        var workSet = mutableSetOf<Pair<Int, Int>>()
        for (pair in set) {
            val ith = intArrayOf(0, 1, -1, 0)
            val jth = intArrayOf(1, 0, 0, -1)
            for (k in ith.indices) {
                val first = pair.first + ith[k]
                val second = pair.second + jth[k]
                val element = Pair(first, second)
                if (isValidIndex(first, second, parsed.size, parsed[0].size)
                    && parsed[first][second] != "#"
                ) {
                    workSet.add(element)
                }
            }
        }
        set = workSet.toMutableSet()
        index++
    }
    println(set.size)
    println(set)



}

private fun isValidIndex(i: Int, j: Int, l: Int, k: Int): Boolean = !(i < 0 || j < 0 || i >= l || j >= k)
