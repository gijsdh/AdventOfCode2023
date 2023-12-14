fun main(args: Array<String>) {
    val input = getResourceAsText("input.txt")
    val inputTest = getResourceAsText("testInput.txt")

    val parsed = input.split("\n\r\n")
        .map {
            it.split("\r")
                .map { it.trim() }
                .map { it.split("").filter { it.isNotEmpty() } }
                .filter { it.isNotEmpty() }
        }

    for (k in listOf(0, 1)) {
        var sum = 0
        for (pattern in parsed) {
            sum += count(pattern, k)
            // transpose the matrix (list<List<T>>), then we can reuse the same algorithm to calculate symmetry
            sum += 100 * count(pattern.transpose(), k)
        }
        println(sum)
    }
}

private fun count(pattern: List<List<String>>, match: Int): Int {
    for (i in 0 until pattern[0].size - 1) { // columns
        var badMatch = 0
        for (j in pattern[0].indices) {
            val left = i - j
            val right = i + 1 + j

            // Maybe there is a smarter way, but
            if (left in 0 until right && right in left + 1 until pattern[0].size) {
                for (k in pattern.indices) {
                    if (pattern[k][left] != pattern[k][right]) {
                        badMatch++
                    }
                }
            }
        }
        if (badMatch == match) {
            return i + 1
        }
    }
    return 0
}

//https://rosettacode.org/wiki/Matrix_transposition#Kotlin
// This type alias is pretty cool.
typealias Matrix<T> = List<List<T>>
fun <T> Matrix<T>.transpose(): Matrix<T> {
    return (0 until this[0].size).map { x ->
        (this.indices).map { y ->
            this[y][x]
        }
    }
}
