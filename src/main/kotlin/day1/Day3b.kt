fun main(args: Array<String>) {
    val inputTest = getResourceAsText("testInput.txt")
    val input = getResourceAsText("input.txt")
    val inputLines = input.lines()

    val map = input.lines().map { it.split(Regex("")).map { it.trim() }.filter { it.isNotEmpty() }.map { it[0] } }
    println(map)

    var used: MutableSet<Pair<Int, Int>> = mutableSetOf();
    var usedInPartOne: MutableSet<Pair<Int, Int>> = mutableSetOf();

    // part 1
    println()
    var sum2=0L
    var sum1=0L
    for (i in 0 until map.size) {
        var j = 0
        while (j < map[0].size) {
            if (map[i][j].isDigit() && !used.contains(Pair(i,j))) {
                val indexesOfNumber = getAllIndexesOfNumber(map, Pair(i, j))

                for (index in indexesOfNumber) {
                    val location = findAdjacentToSpecialCharacter(map, index)
                    if (location != null) {

                        //Part one
                        if(!usedInPartOne.contains(indexesOfNumber[0])) {
                            sum1 += reduceToNumber(indexesOfNumber, map)
                            usedInPartOne.addAll(indexesOfNumber)
                        }

                        val otherNumberIndex = findAdjacentToNumber(map, location, indexesOfNumber)
                        if(otherNumberIndex != null) {
                            val otherNumber = getAllIndexesOfNumber(map, otherNumberIndex)

                            //Add so we don't count double numbers
                            used.addAll(indexesOfNumber)
                            used.addAll(otherNumber)

                            val valueOne = reduceToNumber(indexesOfNumber, map)
                            val valueTwo = reduceToNumber (otherNumber, map)

                            sum1 += valueTwo
                            sum2 += valueOne*valueTwo
                            break
                        }

                    }
                }
            }
            j++
        }
    }
    println(sum1)
    println(sum2)

}

private fun reduceToNumber(list: List<Pair<Int, Int>>, map: List<List<Char>>) = list.map { map[it.first][it.second] }
    .map { it.toString() }
    .reduce { x, y -> x + y }
    .toLong()


private fun getStartNumber(map: List<List<Char>>, index: Pair<Int, Int>): Int {
    var jj = index.second
    val ii = index.first
    while (true) {
        if (!isValidIndex(ii, jj - 1, map.size, map[0].size) || !map[ii][jj - 1].isDigit()) break
        jj--
    }
    return jj
}

private fun getAllIndexesOfNumber(map: List<List<Char>>, index: Pair<Int, Int>): List<Pair<Int, Int>> {
    var list: MutableList<Pair<Int, Int>> = mutableListOf()
    var jj = getStartNumber(map, index)
    while (true) {
        if (!isValidIndex(index.first, jj, map.size, map[0].size) || !map[index.first][jj].isDigit()) break
        list.add(Pair(index.first, jj))
        jj++
    }
    return list
}

private fun findAdjacentToNumber(map: List<List<Char>>, elf: Pair<Int, Int>, list: List<Pair<Int, Int>>): Pair<Int, Int>? {
    val ith = intArrayOf(0, 1, -1, 0, -1, 1, 1, -1)
    val jth = intArrayOf(1, 0, 0, -1, -1, 1, -1, 1)

    for (k in ith.indices) {
        val index_I = elf.first + ith[k]
        val index_J = elf.second + jth[k]
        if (isValidIndex(index_I, index_J, map.size, map[0].size) && !list.contains(Pair(index_I,index_J)) && map[index_I][index_J].isDigit()) {
            return Pair(index_I, index_J)
        }
    }
    return null
}

private fun findAdjacentToSpecialCharacter(map: List<List<Char>>, elf: Pair<Int, Int>): Pair<Int, Int>? {
    val ith = intArrayOf(0, 1, -1, 0, -1, 1, 1, -1)
    val jth = intArrayOf(1, 0, 0, -1, -1, 1, -1, 1)

    for (k in ith.indices) {
        val index_I = elf.first + ith[k]
        val index_J = elf.second + jth[k]
        if (isValidIndex(index_I, index_J, map.size, map[0].size) &&
            !map[index_I][index_J].isDigit() &&
            map[index_I][index_J] != '.')
        {

            return Pair(index_I, index_J)
        }
    }
    return null
}

private fun isValidIndex(i: Int, j: Int, l: Int, k: Int): Boolean {
    if (i < 0 || j < 0 || i >= l || j >= k) return false
    return true
}



