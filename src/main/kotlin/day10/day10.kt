import java.lang.Exception

fun main(args: Array<String>) {
    val input = getResourceAsText("input.txt")
    val inputTest = getResourceAsText("testInput.txt")

    val maze = input.lines().map { it.split("").filter { it.isNotEmpty() }.map { it.trim() } }
    var start = Pair(0, 0)

    for (i in 0 until maze.size) {
        for (j in 0 until maze[0].size) {
            if (maze[i][j] == "S") {
                start = Pair(i, j);
                break;
            }
        }
    }

    var visted = mutableListOf<Pair<Int, Int>>(start)
    var costMap = Array(maze.size) { LongArray(maze[0].size) { 0 } }

    var shapes = mutableMapOf(
        "|" to mutableListOf(Pair(1, 0), Pair(-1, 0)),
        "-" to mutableListOf(Pair(0, 1), Pair(0, 1)),
        "L" to mutableListOf(Pair(1, 0), Pair(0, 1)),
        "J" to mutableListOf(Pair(1, 0), Pair(0, -1)),
        "7" to mutableListOf(Pair(-1, 0), Pair(0, -1)),
        "F" to mutableListOf(Pair(-1, 0), Pair(0, 1))
    )

    var sum2 =0;
    for (starting in shapes.values) {
        var new = Pair(starting[0].first + start.first, starting[0].second + start.second)
        var last = Pair(starting[1].first + start.first, starting[1].second + start.second)

        var sum = 0;
        while (true) {
            sum ++
            println("$new $last" )
            val shape = maze[new.first][new.second]
            println(shape )
            var newStep = when (shape) {
                "|" -> if(last.first > new.first) Pair(new.first -1, new.second) else Pair(new.first + 1, new.second)
                "-" -> if(last.second < new.second) Pair(new.first, new.second +1) else Pair(new.first, new.second - 1)
                "L" -> if(last.first < new.first) Pair(new.first, new.second + 1) else Pair(new.first - 1, new.second )
                "J" -> if(last.first < new.first) Pair(new.first, new.second -1) else Pair(new.first -1, new.second )
                "7" -> if(last.second < new.second) Pair(new.first + 1, new.second ) else Pair(new.first, new.second - 1)
                "F" -> if(last.second > new.second) Pair(new.first + 1, new.second) else Pair(new.first, new.second + 1)
                "." -> break;
                "S" -> break;
                else -> throw Exception("shit")
            }
            last = new
            new = newStep

            if (new == last) break
        }

        if(sum > sum2) sum2= sum
    }

    println(sum2 / 2)


}








