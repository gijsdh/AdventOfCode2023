import java.util.*
import kotlin.Comparator

fun main(args: Array<String>) {
    val input = getResourceAsText("input.txt")
    val inputTest = getResourceAsText("testInput.txt")

    val parsed = input.lines()
        .map {
            it.split("")
                .filter { it.isNotEmpty() }
                .map { it.trim().toInt() }.toTypedArray()
        }.toTypedArray()


    val partOne = findMinimumCostPath(
        parsed,
        Pair(parsed.size - 1, parsed[0].size - 1)
    ) { direction, directionOld, count -> direction == directionOld && count >= 3 }

    println(partOne)

    val partTwo = findMinimumCostPath(
        parsed,
        Pair(parsed.size - 1, parsed[0].size - 1)
    ) { direction, directionOld, count -> (direction == directionOld && count >= 10) || direction != directionOld && count < 4 }

    println(partTwo)
}



private fun findMinimumCostPath(maze: Array<Array<Int>>, finish: Pair<Int, Int>, part: (Int, Int, Int) -> Boolean): Long {

    // A variation on Dijkstra's
    // Maybe it could be more efficient with costMap with also takes into account direction and amount of movement in that direction.
    var visted = mutableSetOf<Triple<Int, Int, Pair<Int, Int>>>()
    var costMap = Array(maze.size) { LongArray(maze[0].size) { 9999999999 } }

    val comparByCost: Comparator<Cost> = compareBy { it.costValue }
    val costQueue = PriorityQueue<Cost>(comparByCost)

    //Initialize start
    // 0 right, 1 down, 2 left, 3 up
    costQueue.add(Cost(0, 0, 0, Pair(0, 0)))
    costQueue.add(Cost(0, 0, 0, Pair(1, 0)))
    costMap[0][0] = 0L

    val ith = intArrayOf(0, 1, -1, 0)
    val jth = intArrayOf(1, 0, 0, -1)
    val directions = intArrayOf(0, 1, 3, 2)

    while (costQueue.isNotEmpty()) {
        val cell: Cost = costQueue.remove()
        val i: Int = cell.index_i
        val j: Int = cell.index_j
        val directionOld = cell.direction.first
        val count = cell.direction.second

        if (visted.contains(Triple(cell.index_i, cell.index_j, cell.direction))) continue
        visted.add(Triple(cell.index_i, cell.index_j, cell.direction))

        for (k in ith.indices) {
            val index_I = i + ith[k]
            val index_J = j + jth[k]
            val direction = directions[k]
            val newCount = if (direction == directionOld) count + 1 else 1

            if (isValidIndex(index_I, index_J, maze.size, maze[0].size)
                && isValidMove(direction, directionOld, count, part)) {
                if (Pair(index_I, index_J) == finish) {
                    return cell.costValue + maze[index_I][index_J]
                }

                costQueue.add(Cost(index_I, index_J, cell.costValue + maze[index_I][index_J], Pair(direction, newCount)))
            }
        }
    }

    return -1
}

fun isValidMove(direction: Int, directionOld: Int, count: Int, part: (Int, Int, Int) -> Boolean): Boolean {
    if(part.invoke(direction, directionOld, count)) return false
    if (direction == 0 && directionOld == 2) return false
    if (direction == 1 && directionOld == 3) return false
    if (direction == 2 && directionOld == 0) return false
    if (direction == 3 && directionOld == 1) return false
    return true
}

class Cost(val index_i: Int, val index_j: Int, val costValue: Long, var direction :Pair<Int, Int>) {
    override fun toString(): String {
        return "$index_i $index_j $costValue ${direction.first} ${direction.second}"
    }
}

private fun isValidIndex(i: Int, j: Int, l: Int, k: Int): Boolean = !(i < 0 || j < 0 || i >= l || j >= k)

private fun printMap(numbers: Array<LongArray>) {
    for (el in numbers) {
        println(el.toList())
    }
    println("--------------------------------------------------------------------------")
}