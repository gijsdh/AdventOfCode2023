fun main(args: Array<String>) {
    val input = getResourceAsText("input.txt")
    val inputLines = input.lines()

    // part 1
    val message = inputLines
        .map { it.filter { it.isDigit() } }
        .map { (it[0].toString() + it[it.length - 1]).toLong() }
        .sum()

    println(message)

    //weird mapping as we need to consider letter overlapping eightwothree = 823
    var map: MutableMap<String, String> = mutableMapOf(
        "one" to "o1ne",
        "two" to "t2wo",
        "three" to "t3hree",
        "four" to "fo4ur",
        "five" to "fi5ve",
        "six" to "si6x",
        "seven" to "sev7en",
        "eight" to "eig8ht",
        "nine" to "ni9ne"
    );

    // part 2 its ugly.
    var sum = 0L
    for (line in inputLines) {
        var newLine =  line
        for (entry in map) {
            if(newLine.contains(entry.key)){
                newLine = newLine.replace(entry.key, entry.value)
            }
        }
        var onlyDigits = newLine
            .filter { it.isDigit() }
            .split("")
            .filter { it.isNotEmpty() }

        sum+=(onlyDigits[0] + onlyDigits[onlyDigits.size - 1]).toLong()
    }
    println(sum)
}


fun getResourceAsText(path: String): String {
    return object {}.javaClass.getResource(path).readText()
}





