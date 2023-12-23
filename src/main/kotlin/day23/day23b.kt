package day23

import getResourceAsText
import java.util.*
import kotlin.math.max


fun main(args: Array<String>) {
    val input = getResourceAsText("input.txt")
    val inputTest = getResourceAsText("testInput.txt")

    val maze = inputTest.lines()
        .map {
            it.split("")
                .filter {
                    it.isNotEmpty()
                }
        }

    var intersections = mutableSetOf<Pair<Int, Int>>()

    // Had to go to Reddit again. Apparently we can make use of the fact that the maze has long single wide paths.
    // So we reduce the problem to a weighted graph. Where all the intersection are the node/ vertices and the edges.
    // (the weight of an edge is the amount of steps we need to take to find another intersection).

    // Underneath we find all the intersections.
    val ith = intArrayOf(0, 1, -1, 0)
    val jth = intArrayOf(1, 0, 0, -1)
    for (i in maze.indices) {
        for (j in maze[0].indices) {
            if (maze[i][j] != "#") {
                var count = 0
                for (k in ith.indices) {
                    val index_I = i + ith[k]
                    val index_J = j + jth[k]
                    if (isValidIndex(index_I, index_J, maze.size, maze[0].size) && maze[index_I][index_J] != "#") count++

                }
                if (count > 2) {
                    intersections.add(Pair(i, j))
                }
            }
        }
    }

    var start = Pair(0, 1);
    end = Pair(maze.size - 1, maze[0].size - 2)
    intersections.add(start)
    intersections.add(end)

    println(intersections)

    var graph = mutableMapOf<Pair<Int, Int>, Node>()

    // Here we create the graph, by walking outwards from the intersections until we find another intersections.
    // So we find the edges and there weight.
    for (intersect in intersections) {
        val edges = mutableListOf<Edge>()
        graph.put(intersect, Node(intersect, edges))

        var dequeue: LinkedList<Triple<Int, Int, Int>> = LinkedList();
        dequeue.add(Triple(intersect.first, intersect.second, 0))

        val visited = mutableSetOf<Pair<Int, Int>>()
        while (dequeue.isNotEmpty()) {
            val cell = dequeue.remove()
            val i: Int = cell.first
            val j: Int = cell.second
            val value = cell.third

            val location = Pair(i, j)

            if (visited.contains(location)) continue
            visited.add(location)

            if (intersections.contains(location) && intersect != location) {
                edges.add(Edge(intersect, location, value))
                continue
            }

            for (k in ith.indices) {
                val index_I = i + ith[k]
                val index_J = j + jth[k]
                if (isValidIndex(index_I, index_J, maze.size, maze[0].size) && maze[index_I][index_J] != "#") {
                    dequeue.add(Triple(index_I, index_J, value + 1))
                }
            }
        }
    }


    // We use a DFS to search through all the paths, apparently the longest path is NP = P problem.
    // So we cannot do anything smart.
    DFS(start, graph, 0)
    println(ans)
}

var ans = 0
var visitedDFS = mutableSetOf<Pair<Int, Int>>()
var end: Pair<Int, Int> = Pair(0, 0)
fun DFS(loc: Pair<Int, Int>, graph: MutableMap<Pair<Int, Int>, Node>, value: Int) {
    if (visitedDFS.contains(loc)) {
        return
    }
    // As DFS is depth first we first store intersection we have already visited, this is to prevent looping.
    visitedDFS.add(loc)

    var node = graph.get(loc)!!
    if (loc == end) {
        ans = max(value, ans)
    }
    for (egde in node.edges) {
        DFS(egde.b, graph, value + egde.distance)
    }

    // As we move upwards again (backtracking) from the BFS we remove the seen, otherwise we would not search all branches to the bottom (end).
    // If we were allowed to move backwards through the grid, there would not be any solution. As we could endlessly cycle in a loop, or move back and forth on the same spot.
    visitedDFS.remove(loc)
}

class Node(val loc: Pair<Int, Int>, var edges: MutableList<Edge>) {
    override fun toString(): String {
        return "Node(loc=$loc, edges=$edges)"
    }

}

class Edge(val a: Pair<Int, Int>, val b: Pair<Int, Int>, val distance: Int) {
    override fun toString(): String {
        return "Edge(a=$a, b=$b, distance=$distance)"
    }
}

private fun isValidIndex(i: Int, j: Int, l: Int, k: Int): Boolean = !(i < 0 || j < 0 || i >= l || j >= k)
