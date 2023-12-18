import java.util.*
import kotlin.Comparator

fun main(args: Array<String>) {
    val input = getResourceAsText("input.txt")
    val inputTest = getResourceAsText("testInput.txt")

    val parsed = input.lines().map { it.split(" ") }
    println(parsed)

    val map: HashMap<String, Pair<Int, Int>> = hashMapOf(
        Pair("R", Pair(0, 1)),
        Pair("L", Pair(0, -1)),
        Pair("U", Pair(-1, 0)),
        Pair("D", Pair(1, 0))
    )
    val set = mutableSetOf<Pair<Int, Int>>(Pair(0, 0))
    val set2 = mutableSetOf<Pair<Int, Int>>(Pair(0, 0))
    for (line in parsed) {
        var direction = line[0]
        var length = line[1].toInt()
        for (i in 0 until length) {
            val pair = map[direction]!!
            val y = set.last().first + pair.first
            val x = set.last().second + pair.second

            set.add(Pair(y, x))
        }
    }


    val dequeue: LinkedList<Pair<Int, Int>> = LinkedList<Pair<Int, Int>>();
    val visited: MutableSet<Pair<Int, Int>> = mutableSetOf()
    println("Flood fill")
    dequeue.add(Pair(1,1))
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

            if (!set.contains(element)) {
                dequeue.add(element)
            }
        }
    }

    println(visited.size + set.size)
}



