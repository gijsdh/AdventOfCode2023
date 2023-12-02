fun main(args: Array<String>) {
    val input = getResourceAsText("input.txt")
    val inputLines = input
        .lines()
        .map {
            it.split("")
               .map { it.trim() }
               .filter { it.isNotEmpty() }
        }

    var sum =0
    for (line in inputLines) {
        var hasDouble = false
        var hasDuplicate = false
        for ((index, char) in line.withIndex()) {
            if (index + 1 < line.size) {
                val duplicate = line[index] + line[index + 1]
                for (j in IntRange(index + 2, line.size - 2)) {
                    if (hasDuplicate) break
                    hasDuplicate = duplicate == (line[j] + line[j + 1])
                }
            }
            if (!hasDouble && index + 2 < line.size) hasDouble = line[index] == line[index + 2];
        }
        if(hasDouble && hasDuplicate) sum++
    }

    println(sum)

}







