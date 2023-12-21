import java.util.*
import kotlin.Comparator

fun main(args: Array<String>) {
    val input = getResourceAsText("input.txt")
    val inputTest = getResourceAsText("testInput.txt")

    val parsed = input.lines().map { it.split("->").map { it.trim() } }
    println(parsed)


    var map = mutableMapOf<String, Module>()
    for (line in parsed) {
        val name = line[0].drop(1)
        val type = line[0][0].toString()
        var emptyMap = mutableMapOf<String, String>()
        val childs = line[1].split(",").map { it.trim() }
        if (line[0].startsWith("%")) {
            map.put(name, Module(name, type, childs, "off", emptyMap))
        } else if (line[0].startsWith("&")) {
            map.put(name, Module(name, type, childs, "low", emptyMap))
        } else {
            map.put(name, Module(name, type, childs, "", emptyMap))
        }
    }

    for (value in map.values) {
        for (child in value.childs) {
            if (map[child] == null) continue
            if (map[child]!!.type == "&") {
                map[child]!!.mem.put(value.name, "low")
            }

        }
    }
    println(map)


    var sumLow = 0
    var sumHigh = 0
    var index = 0L;

    // From input: All points below point towards cl is Conjunction point towards rx. So it needs 4 High pulses to send low pulse.
    var cycleMape = mutableMapOf("js" to 0L, "qs" to 0L, "dt" to 0L, "ts" to 0L)

    while (true) {
        index++

        var state = "roadcaster"
        var dequeue: LinkedList<Pair<Module, Pair<String, String>>> = LinkedList();
        dequeue.add(Pair(map[state]!!, Pair("low", "old")))
        sumLow++

        while (dequeue.isNotEmpty()) {
            val work: LinkedList<Pair<Module, Pair<String, String>>> = LinkedList();
            while (dequeue.isNotEmpty()) {

                val current = dequeue.pop()
                val module = current.first
                var pulse = current.second.first
                val old = current.second.second

                if(pulse == "low" && module.name == "rx") throw Exception("$index")

                if (module.state in listOf("low", "high")) {
                    map[module.name]!!.mem[old] = pulse
                    pulse = if(map[module.name]!!.mem.values.all { it == "high" }) "low" else "high"
                }


                if (pulse == "high" && module.name in cycleMape.keys) {
                    if (cycleMape[module.name] == 0L) {
                        cycleMape[module.name] = index
                    }
                    if (cycleMape.values.all { it > 0 }) throw java.lang.Exception("${lcm(cycleMape.values.toList())}")
                }

                if (pulse == "high" && module.state in listOf("on", "off")) continue

                if (module.state == "on" && pulse == "low") {
                    map[module.name]!!.state = "off"
                    pulse = "low"
                } else if (module.state == "off" && pulse == "low") {
                    map[module.name]!!.state = "on"
                    pulse = "high"
                }

                for (child in module.childs) {
                    if (pulse == "low") sumLow++ else sumHigh++
//                    println("${module.name} ${pulse}1 $child")
                    if (map[child] == null) continue

                    var target = map[child]!!
                    work.add(Pair(target, Pair(pulse, module.name)))

                }
            }
            dequeue.addAll(work)
        }

        if(index == 1000L) println("${sumLow*sumHigh}")

    }
}

class Module(val name:String, val type: String, val childs: List<String>, var state: String, val mem: MutableMap<String,String>) {
    override fun toString(): String {
        return "Module(name='$name', type='$type', childs=$childs, state='$state', mem=$mem)"
    }


}


