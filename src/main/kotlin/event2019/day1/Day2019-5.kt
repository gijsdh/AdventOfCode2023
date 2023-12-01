fun main(args: Array<String>) {
    val input = getResourceAsText("input.txt")
    val inputLines = input
        .lines()
        .map {
            it.split("")
               .map { it.trim() }
               .map { it.toString() }
               .filter { it.isNotEmpty() }

        }

    val setVowels =  mutableSetOf("a", "e", "i", "o", "u")
    val setLetter =  mutableSetOf("ab", "cd", "pq", "xy")

    var sum =0
    for (line in inputLines) {
        var countvowels = 0
        var hasDouble = false
        for ((index, char) in line.withIndex()) {
            if (setVowels.contains(char)) countvowels++
            if (index + 1 < line.size && setLetter.contains(line[index] + line[index + 1]))  break;
            if (!hasDouble && index + 1 < line.size) hasDouble = line[index] == line[index + 1]

            if (countvowels > 2 && hasDouble && index + 1 == line.size) {
                sum++
                break
            }
        }
    }

    println(sum)

}







