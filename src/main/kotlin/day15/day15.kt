fun main(args: Array<String>) {
    val input = getResourceAsText("input.txt")
    val inputTest = getResourceAsText("testInput.txt")

    val parsed = input.lines()[0].split(",").map { it.trim() }

    var sum = 0
    for (line in parsed) {
        var lineHash = 0
        for (char in line) {
            var code = char.code
            lineHash += code
            lineHash *= 17
            lineHash %= 256
        }
        sum += lineHash
    }

    println(sum)
}