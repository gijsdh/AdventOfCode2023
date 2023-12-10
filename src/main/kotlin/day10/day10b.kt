import java.lang.Exception
import java.util.*

fun main(args: Array<String>) {
    val input = getResourceAsText("input.txt")
    val inputTest = getResourceAsText("testInput.txt")

    val maze = inputTest.lines().map { it.split("").filter { it.isNotEmpty() }.map { it.trim() } }
    var start = Pair(0, 0)

    for (i in 0 until maze.size) {
        for (j in 0 until maze[0].size) {
            if (maze[i][j] == "S") {
                start = Pair(i, j);
                break;
            }
        }
    }

    var shapes = mutableMapOf(
        "|" to Pair(-1, 0),
        "-" to Pair(0, 1),
        "L" to Pair(0, 1),
        "J" to Pair(0, -1),
        "7" to Pair(0, -1),
        "F" to Pair(0, 1)
    )


    // Getting starting shape.
    val up = listOf("|", "7", "F").contains(maze[start.first - 1][start.second])
    val down = listOf("|", "L", "J").contains(maze[start.first + 1][start.second])
    val right = listOf("-", "7", "J").contains(maze[start.first][start.second + 1])
    val left = listOf("-", "F", "L").contains(maze[start.first][start.second - 1])
    val startShape = when {
        up && right -> "L"
        up && left -> "J"
        down && right -> "F"
        down && left -> "7"
        up && down -> "|"
        right && left -> "-"
        else -> throw Exception("shit")
    }

    println("Calculating loop")


    var new = Pair(shapes[startShape]!!.first + start.first, shapes[startShape]!!.second + start.second)
    var last = start
    var sum = 0;
    var positions = mutableSetOf(new)
    while (true) {
        sum++
        val shape = maze[new.first][new.second]
        if (shape == "S") break
        val newStep = getNewStep(shape, last, new)
        last = new
        new = newStep

        if (positions.contains(newStep)) break
        positions.add(new)
    }

    //Answer 1
    println("Answer 1 = ${sum / 2}")

    val loop = positions.toSet()
    var newMaze = maze.map { it.toMutableList() }.toMutableList();

    println("Replacing all other elements except the loop")
    for (i in 0 until maze.size) {
        for (j in 0 until maze[0].size) {
            if (!loop.contains(Pair(i, j))) newMaze[i][j] = "."
            if (newMaze[i][j] == "S") newMaze[i][j] = startShape
        }
    }

    println("Creating a big Maze")
    // we do replacements here to be able to use the flood fill algorithm, I have no idea how to do it otherwise.
    //
    //   . ->   .  .    L    -> L  -   7   ->   7  .   J ->   J  .  ect
    //          .  .            .  .            |  .          .  .
    var bigMaze: Array<Array<String>> = Array(maze.size * 2) { Array(maze[0].size * 2) { "." } }
    var replaceMents = mapOf(
        "|" to listOf("|", ".", "|", "."),
        "." to listOf(".", ".", ".", "."),
        "-" to listOf("-", "-", ".", "."),
        "L" to listOf("L", "_", ".", "."),
        "7" to listOf("7", ".", "|", "."),
        "F" to listOf("F", "-", "|", "."),
        "J" to listOf("J", ".", ".", "."),
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

    printMap(bigMaze)


    val dequeue: LinkedList<Pair<Int, Int>> = LinkedList<Pair<Int, Int>>();
    val visited: MutableSet<Pair<Int, Int>> =  mutableSetOf()

    // We start searching from all corners, in case loop touches one of the corners.
    dequeue.add(Pair(0, 0))
    dequeue.add(Pair(0, bigMaze[0].size - 1))
    dequeue.add(Pair(bigMaze.size - 1, 0))
    dequeue.add(Pair(bigMaze.size - 1, bigMaze[0].size - 1))

    println("Flood fill")
    while (dequeue.isNotEmpty()) {
        var nextCube = dequeue.pollFirst()

        if (visited.contains(nextCube)) continue
        visited.add(nextCube)

        //Flood fill algorithm, we continuously check next cells if they are a dot.
        val ith = intArrayOf(0, 1, -1, 0)
        val jth = intArrayOf(1, 0, 0, -1)
        for (k in ith.indices) {
            val first = nextCube.first + ith[k]
            val second = nextCube.second + jth[k]
            val element = Pair(first, second)

            if (isValidIndex(first, second, bigMaze.size, bigMaze[0].size) && bigMaze[first][second] == ".") dequeue.add(element)
        }
    }

    println("Big maze has ${visited.size} elements outside the loop")

    println("Counting enclosed")
    // Maybe something more smart is possible, now it just loop over whole thing and only count dots if we find four dots together like =  .  .
    // and after counting marking them as visited.                                                                                         .  .
    var sumEnd = 0
    for (i in 0 until bigMaze.size) {
        for (j in 0 until bigMaze[0].size) {
            if (!visited.contains(Pair(i, j)) && bigMaze[i][j] == ".") {
                if (isDotInOldMaze(i, j, bigMaze)) {
                    sumEnd++
                    visited.add(Pair(i, j))
                    visited.add(Pair(i + 1, j + 1))
                    visited.add(Pair(i, j + 1))
                    visited.add(Pair(i + 1, j))
                }
            }
        }
    }
    println("Answer part 2= $sumEnd")
    println("all visited ${visited.size}")
    println("Size of bigMaze ${bigMaze.size*bigMaze[0].size}")

}

// A bit ugly and prone to error but it works.
private fun getNewStep(shape: String, last: Pair<Int, Int>, new: Pair<Int, Int>): Pair<Int, Int> {
    return when (shape) {
        "|" -> if (last.first > new.first) Pair(new.first - 1, new.second) else Pair(new.first + 1, new.second)
        "-" -> if (last.second < new.second) Pair(new.first, new.second + 1) else Pair(new.first, new.second - 1)
        "L" -> if (last.first < new.first) Pair(new.first, new.second + 1) else Pair(new.first - 1, new.second)
        "J" -> if (last.first < new.first) Pair(new.first, new.second - 1) else Pair(new.first - 1, new.second)
        "7" -> if (last.second < new.second) Pair(new.first + 1, new.second) else Pair(new.first, new.second - 1)
        "F" -> if (last.second > new.second) Pair(new.first + 1, new.second) else Pair(new.first, new.second + 1)
        else -> throw Exception("shit")
    }
}

private fun isDotInOldMaze(i: Int, j: Int, bigMaze: Array<Array<String>>): Boolean {
    for (k in 0 until 4) {
        val ith = intArrayOf(0, 0, 1, 1)
        val jth = intArrayOf(0, 1, 0, 1)
        val first = i + ith[k]
        val second = j + jth[k]
        if (isValidIndex(first, second, bigMaze.size, bigMaze[0].size) && bigMaze[first][second] != ".") return false
    }
    return true
}

private fun printMap(numbers: Array<Array<String>>) {
    for (el in numbers) {
        println(el.toList())
    }
    println("--------------------------------------------------------------------------")
}

private fun isValidIndex(i: Int, j: Int, l: Int, k: Int): Boolean {
    if (i < 0 || j < 0 || i >= l || j >= k) return false
    return true
}




