fun main(args: Array<String>) {
    val input = getResourceAsText("input.txt")
    val inputTest = getResourceAsText("testInput.txt")

    val parsed = input.lines()
        .map { it.split(" ").map { it.split(",").filter { it.isNotEmpty() } } }

    var sum = 0
    for (line in parsed) {
        var options = mutableListOf<List<String>>()
        var element = line[0][0]
            .replace(Regex("[.]+"), ".")
            .split("").filter { it.isNotEmpty() }
        println(element)

        options.add(element)
        for ((j, char) in element.withIndex()) {
            if (char != "?") continue

            val temp = mutableListOf<List<String>>()
            for (list in options) {
                val optionOne = list.toMutableList()
                val optionTwo = list.toMutableList()
                optionOne[j] = "#"
                optionTwo[j] = "."

                temp.add(optionOne)
                temp.add(optionTwo)
            }
            options = temp
        }


        for (list in options) {
            var count = 0
            if (list.count { it == "#" } != line[1].sumOf { it.toInt() }) continue

            var counts = mutableListOf<Int>()
            for (letter in list) {
                if (letter == "#") {
                    count++
                } else if (count > 0) {
                    counts.add(count)
                    count = 0
                }
            }

            if (counts.contains(0)) throw Exception("shit")
            if (count > 0) counts.add(count)

            if (line[1].map { it.toInt() } == counts) {
                println(counts)
                sum++
            }
        }
        println("----")
    }
    println(sum)
}







