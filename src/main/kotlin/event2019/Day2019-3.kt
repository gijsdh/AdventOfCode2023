fun main(args: Array<String>) {
    val input = getResourceAsText("input.txt")
    val splits = input.split("").map { it.trim() }.filter { it.isNotEmpty() }
    println(splits)

    val map = mutableSetOf(Pair(0, 0))
    var loc: Pair<Int, Int> = Pair(0, 0)
    var loc2: Pair<Int, Int> = Pair(0, 0)
    val mapSet: HashMap<Pair<Int,Int>, Int> = hashMapOf();
    mapSet[Pair(0, 0)] = 1

    val directions: HashMap<String, Pair<Int, Int>> = hashMapOf(
        Pair(">", Pair(1, 0)),
        Pair("<", Pair(-1, 0)),
        Pair("^", Pair(0, 1)),
        Pair("v", Pair(0, -1))
    )
    for ((index, line) in splits.withIndex()) {
        if (index % 2 == 0) {
            loc = pair(directions, line, loc)
            map.add(loc)
        } else {
            loc2 = pair(directions, line, loc2)
            map.add(loc2)
        }
    }
    println(map.size)
}

private fun pair(
    directions: HashMap<String, Pair<Int, Int>>,
    line: String,
    loc: Pair<Int, Int>
): Pair<Int, Int> {
    var loc1 = loc
    val incrementPair = directions[line]!!
    var x = loc1.first + incrementPair.first
    var y = loc1.second + incrementPair.second
    loc1 = Pair(x, y)
    return loc1
}









