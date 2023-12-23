package day23

import getResourceAsText
import java.util.*
import kotlin.Comparator
import kotlin.math.min


fun main(args: Array<String>) {
    val input = getResourceAsText("input.txt")
    val inputTest = getResourceAsText("testInput.txt")

    val parsed = input.lines()
        .map {
            it.split("")
                .filter {
                    it.isNotEmpty()
                }
        }
    println(parsed)
    println(
        findMinimumCostPath(
            parsed.map { it.toTypedArray() }.toTypedArray(),
            Pair(0, 1),
            Pair(parsed.size - 1, parsed[0].size - 2)
        )
    )
}

// Really inefficient way of solving it as I am storing its entire Path.
// As it is a maze like structure it does not matter that much, as there are not that many paths in the data.
private fun findMinimumCostPath(maze: Array<Array<String>>, start: Pair<Int, Int>, finish: Pair<Int, Int>): Long {
//    var visted = Array(maze.size) { BooleanArray(maze[0].size) { false } }
    val visted = mutableSetOf<Pair<Int,Int>>()
    var costMap = Array(maze.size) { LongArray(maze[0].size) { 9999999999 } }

    val comparByCost: java.util.Comparator<Cost> = compareBy { it.costValue }
    val costQueue = PriorityQueue<Cost>(comparByCost)


    //Initialize start
    costQueue.add(Cost(start.first, start.second, 0, visted))
    costMap[start.first][start.second] = 0L
    val ith = intArrayOf(0, 1, -1, 0)
    val jth = intArrayOf(1, 0, 0, -1)

    val results = mutableListOf<Long>()
    while (costQueue.isNotEmpty()) {

        val cell: Cost = costQueue.remove()
        var visited = cell.visited
        val i: Int = cell.index_i
        val j: Int = cell.index_j

        if (visited.contains(Pair(i,j))) continue
        visited.add(Pair(i,j))


        for (k in ith.indices) {
            val index_I = i + ith[k]
            val index_J = j + jth[k]

            if (isValidIndex(index_I, index_J, maze.size, maze[0].size) && !visited.contains(Pair(index_I,index_J)) &&
                isValidMove(k, maze, index_I, index_J)
            ) {
                if (Pair(index_I, index_J) == finish) {
                    println(cell.costValue - 1)
                    results.add(cell.costValue - 1)
                }

                costMap[index_I][index_J] =
                    min(costMap[index_I][index_J], cell.costValue - 1)
                costQueue.add(Cost(index_I, index_J, cell.costValue - 1, visited.toMutableSet()))
            }
        }
    }
    printMap(costMap)
    println(results.min())
    return -1
}

fun isValidMove(k: Int, maze: Array<Array<String>>, indexI: Int, indexJ: Int): Boolean {
    if (maze[indexI][indexJ] != "#") return true
    if (maze[indexI][indexJ] == ">" && k == 0) return true
    if (maze[indexI][indexJ] == "<" && k == 3) return true
    if (maze[indexI][indexJ] == "v" && k == 1) return true
    if (maze[indexI][indexJ] == "^" && k == 2) return true
    return false
}

class Cost(val index_i: Int, val index_j: Int, val costValue: Long, val visited: MutableSet<Pair<Int, Int>>) {
    override fun toString(): String {
        return "$index_i $index_j $costValue"
    }
}

private fun isValidIndex(i: Int, j: Int, l: Int, k: Int): Boolean = !(i < 0 || j < 0 || i >= l || j >= k)
private fun printMap(numbers: Array<LongArray>) {

    for (el in numbers) {
        for (number in el) {
            if (number == 9999999999) print("#   ") else print(number.toString().padEnd(4, ' ' ))
        }
        println()
    }
    println("--------------------------------------------------------------------------")
}

private fun printMap(numbers: Array<BooleanArray>) {

    for (el in numbers) {
        println(el.toList())
    }
    println("--------------------------------------------------------------------------")
}