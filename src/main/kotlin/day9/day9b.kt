import java.lang.Exception
import java.lang.Math.abs

fun main(args: Array<String>) {
    val input = getResourceAsText("input.txt")
    val inputTest = getResourceAsText("testInput.txt")

    val parsed = input.lines().map { it.split(" ").filter { it.isNotEmpty() }.map { it.trim().toLong() } }
    println(parsed)

    var sum = 0L
    for (line in parsed) {

        var dif = 1L
        var index = 0
        var list = mutableListOf<List<Long>>(line)
        while (true) {
            list.add(list[index]
                .windowed(2)
                .map { it[0] - it[1] }
            )
            if (list[index + 1].all { it == 0L }) break
            index++
        }

        sum += list.map { it.first() }.reduce { i, j -> i + j }

    }
    println(sum)
}





