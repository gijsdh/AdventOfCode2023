package day25

import getResourceAsText
import java.util.*
import kotlin.math.min

fun main(args: Array<String>) {
    val input = getResourceAsText("input.txt")
    val inputTest = getResourceAsText("testInput.txt")

    val parsed =
        inputTest.lines().map { it.split(Regex(": | ")) }

    println(parsed)
    var graph: MutableMap<String, MutableMap<String, Int>> = mutableMapOf()

    for (line in parsed) {
        val key = line[0]
        for (i in 1 until line.size) {
            val edge = line[i]
            graph.merge(key, mutableMapOf(edge to 1)) { i, j -> (i + j).toMutableMap() }
            graph.merge(edge, mutableMapOf(key to 1)) { i, j -> (i + j).toMutableMap()  }
        }
    }

    println(graph)


   var graphToSearch =  Graph(graph, graph.keys.map { it to null }.toMap().toMutableMap())


    // We use here the fact that in a maximum flow problem, if there are only 3 edge connecting two larger groups.
    // The maximum flow will be 3 if source is in one group and sink is in the other. As all nodes have a weight / capacity of 3.
    //

    val s = graph.keys.first()
    for (t in graph.keys.drop(1)) {
        if (graphToSearch.minCut(s, t) == 3) {
            println("$s $t")
            break
        }
    }
    // When we stop teh calculation at the first ocurrence of mincut, we get back a parent of the last BFS.
    // In this BFS we found that we could not move to the other group anymore.
    // As all three vertices were used in previous searches. So parent will only contain all nodes in One group, the other nodes will be undefined.
    val groupOne = graphToSearch.parent.filter { it.value != null }
    val groupTwo = graphToSearch.parent.filter { it.value == null }

    println(groupTwo)
    println(groupOne)
    println("Answer: ${groupOne.size*groupTwo.size}")

}

class Graph(val graph: MutableMap<String, MutableMap<String, Int>>, var parent: MutableMap<String, String?>) {

    // Underneath is the ford fulkerson algorithm, implemented by following a youtube video following a guy from Reddit.
    // Its a way to solve maximum flow or minimum cut. Have a bit of reading to do.
    // Again not being able to solve without help, thanks for Anshuman Dash for the nice informative video.
    // Looking back at other years, this was a hard day 25.
    fun BFS(s: String, t: String): Boolean {
        parent = graph.keys.map { it to null }.toMap().toMutableMap()
        parent[s] = s
        val dequeue: LinkedList<String> = LinkedList<String>();
        dequeue.add(s)

        // Just walks through the grid. And we keep track of Parent (Just a relation between nodes) map to keep track of visited nodes.
        // In the graph we keep track of if we used and edge already, then the capacity would 0 for example.
        // At the end we check if we found the sink as a parent somewhere.
        while (dequeue.isNotEmpty()) {
            var value = dequeue.pollFirst()

            for (ele in graph[value]!!) {
                if (ele.value > 0 && parent[ele.key] == null) {
                    parent[ele.key] = value
                    dequeue.add(ele.key)
                }
            }
        }
        return parent[t] != null
    }


    fun minCut(s: String, t: String): Int {
        // reset graph
        for (e in graph) {
            for (v in e.value) {
                graph[e.key]!![v.key] = 1
            }
        }
//        println(graph)
        var maxflow = 0

        while (BFS(s, t)) {
            var flow = Int.MAX_VALUE
            var n = t

            // This always returns one. But in weighted flow would return, the minimum in the path taken, as that constricts the flow for the whole path.
            while (n != s) {
                flow = min(flow, graph[parent[n]]!![n]!!)
                n = parent[n]!!
            }
            maxflow += flow

            var v = t
            //  Here we update the graph for the path taken, so we would move all the path taken to zero.
            //  As we have bidirectional, we could use a path from both side. In that case we would reverse the flow (+ 1 in this case).
            while (v != s) {
                var u = parent[v]!!
                graph[u]!![v] = graph[u]!![v]!! - flow
                graph[v]!![u] = graph[v]!![u]!! + flow
                v = u
            }
        }
        return maxflow
    }
}