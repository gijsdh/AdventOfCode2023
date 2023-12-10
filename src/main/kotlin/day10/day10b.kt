import java.lang.Exception
import java.util.*

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


    var maps = mutableListOf<Pair<Int, MutableList<Pair<Int, Int>>>>()

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

        var positions = mutableListOf(new);

        while (true) {
            sum ++
            val shape = maze[new.first][new.second]
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
            positions.add(new)
        }
        maps.add(Pair(sum, positions))
        if(sum > sum2) sum2= sum
    }

    println(sum2 / 2)

    val loop = maps.maxBy { it.first }.second.toSet()

    var newMaze = maze.map { it.toMutableList() }.toMutableList();
    for (i in 0 until maze.size) {
        for (j in 0 until maze[0].size) {
            if (!loop.contains(Pair(i, j))) newMaze[i][j] = "."
        }
    }

    var bigMaze: Array<Array<String>> = Array(maze.size * 2) { Array<String>(maze[0].size * 2) { "." } }

    var replaceMents = mapOf(
        "|" to listOf("|", ".", "|", "."),
        "." to listOf(".", ".", ".", "."),
        "-" to listOf("-", "-", ".", "."),
        "L" to listOf("L", "_", ".", "."),
        "7" to listOf("7", ".", "|", "."),
        "F" to listOf("F", "-", "|", "."),
        "J" to listOf("J", ".", ".", "."),
        "S" to listOf("7", ".", "|", ".")
//        "S" to listOf("F", "-", "|", ".")//listOf("7", ".", "|", "."),
    )

    for (i in 0 until maze.size) {
        for (j in 0 until maze[0].size) {
            var replacement = replaceMents[newMaze[i][j]]
            if (replacement == null) println(newMaze[i][j])
            for (k in 0 until 4) {
                val ith = intArrayOf(0, 0, 1, 1)
                val jth = intArrayOf(0, 1, 0, 1)
                val index_I = i * 2 + ith[k]
                val index_J = j * 2 + jth[k]
                bigMaze[index_I][index_J] = replacement!![k]
            }
        }
    }

    printNumber(bigMaze)


    var dequeue: LinkedList<Pair<Int, Int>> = LinkedList<Pair<Int, Int>>();
    var visited: MutableList<Pair<Int, Int>> =  mutableListOf()

    dequeue.add(Pair(0, 0))
    dequeue.add(Pair(0, bigMaze[0].size - 1))
    dequeue.add(Pair(bigMaze.size - 1, 0))
    dequeue.add(Pair(bigMaze.size - 1, bigMaze[0].size - 1))


    while (dequeue.isNotEmpty()) {
        var nextCube = dequeue.pollFirst()

        if (visited.contains(nextCube)) continue
        visited.add(nextCube)

        //Flood fill algorithm, we continuously move outwards and check we that we are not in an enclosed area.
        val ith = intArrayOf(0, 1, -1, 0)
        val jth = intArrayOf(1, 0, 0, -1)
        for (k in ith.indices) {
            val first = nextCube.first + ith[k]
            val second = nextCube.second + jth[k]
            val element = Pair(first, second)

            if (isValidIndex(first, second, bigMaze.size, bigMaze[0].size) && bigMaze[first][second] == ".") dequeue.add(element)
        }
    }

    println(visited.size)

    var sumEnd = 0
    for (i in 0 until bigMaze.size) {
        for (j in 0 until bigMaze[0].size) {

            if (visited.contains(Pair(i, j)) || bigMaze[i][j] != ".") continue
            if (extracted(i, j, bigMaze)) {
                sumEnd++
                visited.add(Pair(i, j))
                visited.add(Pair(i + 1, j + 1))
                visited.add(Pair(i, j + 1))
                visited.add(Pair(i + 1, j))
            }
        }
    }
    println(sumEnd)

    println(visited.size)
    println(bigMaze.size*bigMaze[0].size)

}

private fun extracted(i: Int, j: Int, bigMaze: Array<Array<String>>): Boolean {
    for (k in 0 until 4) {
        val ith = intArrayOf(0, 0, 1, 1)
        val jth = intArrayOf(0, 1, 0, 1)
        val first = i + ith[k]
        val second = j + jth[k]
        if (isValidIndex(first, second, bigMaze.size, bigMaze[0].size) && bigMaze[first][second] != ".") return false
    }
    return true
}

private fun printNumber(numbers: MutableList<MutableList<String>>) {
    for (el in numbers) {
        println(el.toList())
    }
    println("--------------------------------------------------------------------------")
}

private fun printNumber(numbers: Array<Array<String>>) {
    for (el in numbers) {
        println(el.toList())
    }
    println("--------------------------------------------------------------------------")
}

private fun isValidIndex(i: Int, j: Int, l: Int, k: Int): Boolean {
    if (i < 0 || j < 0 || i >= l || j >= k) return false
    return true
}




