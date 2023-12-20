fun main(args: Array<String>) {
    val input = getResourceAsText("input.txt")
    val inputTest = getResourceAsText("testInput.txt")

    val parsed = input.lines().map { it.split(" ") }
    println(parsed)

    val map: HashMap<String, Pair<Int, Int>> = hashMapOf(
        Pair("0", Pair(0, 1)),
        Pair("2", Pair(0, -1)),
        Pair("3", Pair(-1, 0)),
        Pair("1", Pair(1, 0))
    )

    var partTwo: MutableList<Point> = mutableListOf()


    var loc = Pair(0L, 0L)
    var sumLengths = 0L
    for (line in parsed) {
        val direction = line[2][7].toString()
        val length = line[2].subSequence(2, 7).toString().toLong(16)

        sumLengths += length

        val pair = map[direction]!!
        val y = loc.first + (pair.first * length)
        val x = loc.second + (pair.second * length)

        loc = Pair(y, x)
        partTwo.add(Point(loc.first, loc.second))
    }

    println(partTwo)
    println(sumLengths)


    // Learned some new stuff today. Probably revisit day 10 with this.
    // Used Shoelace  https://en.wikipedia.org/wiki/Shoelace_formula to calculate area
    // And we can use Pick's theorem (https://en.wikipedia.org/wiki/Pick%27s_theorem) to calculate the interior points (area).
    // A = I + b / 2 -1  -> I = - b /2 + A + 1
    // Underneath is needed as our line enclosing the area also hase a width.
    // Interior + Exterior =  answers
    val interiorSum = shoelaceArea(partTwo) - sumLengths / 2 + 1

    println(interiorSum + sumLengths)

}

// https://rosettacode.org/wiki/Shoelace_formula_for_polygonal_area
fun shoelaceArea(v: List<Point>): Long {
    val n = v.size
    var a = 0L
    for (i in 0 until n - 1) {
        a += v[i].x * v[i + 1].y - v[i + 1].x * v[i].y
    }
    return Math.abs(a + v[n - 1].x * v[0].y - v[0].x * v[n - 1].y) / 2
}
class Point(val x: Long, val y: Long) {
    override fun toString() = "($x, $y)"
}