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
    var start: Pair<Int, Int>
    for (i in parsed.indices) {
        for (j in parsed[0].indices) {
            if (parsed[i][j] == "S") {
                start = Pair(i, j)
                set.add(start)
            }
        }
    }

    println(set)
    println(parsed.size)


    val X = listOf(0, 1, 2)
    val Y = mutableSetOf<Int>()

    val seen = mutableSetOf<Pair<Int, Int>>()
    val evenSet = mutableSetOf<Pair<Int,Int>>()
    val oddSet = mutableSetOf<Pair<Int,Int>>()

    for(index in 1 until 65 + (parsed.size *2) + 1){

        var workSet = mutableSetOf<Pair<Int, Int>>()
        for (pair in set) {
            val ith = intArrayOf(0, 1, -1, 0)
            val jth = intArrayOf(1, 0, 0, -1)

            // This is to speed it up. Making use of that every two steps, we are back at starting location. (0,0) -> (0,1) -> (0,0)
            // So the only new positions happen at the edges. So we can stop calculating al those spots.
            // As we need to know odd and even results thus 2 sets of results are being maintained.
            // So after 64 steps, we have the even set + all the positions in the workset. the work set should only contain positions at the edges.
            if ((index - 1) % 2 == 0) evenSet.add(pair)
            if (index % 2 == 0) oddSet.add(pair)

            for (k in ith.indices) {
                val first = (pair.first + ith[k])
                val second = (pair.second + jth[k])

                val element = Pair(first, second)
                if (parsed[mod(first, parsed.size)][mod(second, parsed[0].size)] != "#" && element !in seen) {
                    seen.add(element)
                    workSet.add(element)
                }
            }
        }
        set = workSet.toMutableSet()

        val even = evenSet.size + set.size
        val odd = oddSet.size + set.size
        if (index == 64) println("Answer A: $even")

        if (index % parsed.size == 65) {
            if (index % 2 == 0) Y.add(even) else Y.add(odd)
            println(Y)
        }
    }


    // Needed reddit for underneath.
    // Output for part two is used to create quadratic equation.
    // Answer should be (x0 = 3738, x1 = 33270, x2 = 92194)
    // Then use WolframAlpha to fit it to quadratic equation and apply it at 202300
    // This comes form the fact that, (26501365 - 65) / 131 = 202300
    // So the fx we found is f(x) = 65 + 131 x -> is

    // found some regression algo at rosseta's code
    polyRegression(X.toIntArray(),Y.toIntArray(), 202300)
}

private fun mod(first: Int, length: Int): Int {
    val mod = first % length
    return if (mod < 0) mod + length else mod
}


//https://rosettacode.org/wiki/Polynomial_regression#Kotlin
fun polyRegression(x: IntArray, y: IntArray, valueX: Int) {
    val xm = x.average()
    val ym = y.average()
    val x2m = x.map { it * it }.average()
    val x3m = x.map { it * it * it }.average()
    val x4m = x.map { it * it * it * it }.average()
    val xym = x.zip(y).map { it.first * it.second }.average()
    val x2ym = x.zip(y).map { it.first * it.first * it.second }.average()

    val sxx = x2m - xm * xm
    val sxy = xym - xm * ym
    val sxx2 = x3m - xm * x2m
    val sx2x2 = x4m - x2m * x2m
    val sx2y = x2ym - x2m * ym

    val b = (sxy * sx2x2 - sx2y * sxx2) / (sxx * sx2x2 - sxx2 * sxx2)
    val c = (sx2y * sxx - sxy * sxx2) / (sxx * sx2x2 - sxx2 * sxx2)
    val a = ym - b * xm - c * x2m

    fun abc(xx: Int) = a + b * xx + c * xx * xx

    println("Answer B: ${abc(valueX).toLong()}")
    println("y = $a + ${b}x + ${c}x^2\n")
    println(" Input  Approximation")
    println(" x   y     y1")
    for ((xi, yi) in x zip y) {
        System.out.printf("%2d %3d  %5.1f\n", xi, yi, abc(xi))
    }
}
