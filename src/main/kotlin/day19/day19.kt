import java.lang.Exception

fun main(args: Array<String>) {
    val input = getResourceAsText("input.txt")
    val inputTest = getResourceAsText("testInput.txt")

    val parsed = input.split("\n\r\n")
        .map {
            it.split("\r")
                .map { it.trim() }
                .filter { it.isNotEmpty() }
        }

    println(parsed)
    var map = mutableMapOf<String, List<WorkFlow>>()

    for (line in parsed[0]) {
        val lineSplitted = line.split(Regex("\\{|}")).filter { it.isNotEmpty() }
        val key = lineSplitted[0]
        var list = mutableListOf<WorkFlow>()
        for (splitted in lineSplitted[1].split(",")) {
            val split = splitted.split(":")
            if (split.size > 1) {
                var newParts = split.first().split(Regex("<|>"))
                var op = split.first()[1].toString()
                list.add(WorkFlow(op, newParts.first(), newParts.last().toLong(), true, split[1]))
            } else {
                list.add(WorkFlow("", "", 0, true, split[0]))
            }
        }
        map.put(key, list)
    }
    println(map)

    var sum = 0
    for (line in parsed[1]) {
        var parts = line.subSequence(1, line.length - 1).split(",").map { Pair(it.split("=")[0], it.split("=")[1].toInt()) }.toMap()
        if (add(parts, map)) {
            println(line)
            for (part in parts) sum += part.value
        }
    }
    println(sum)

}

private fun add(parts: Map<String, Int>, map: MutableMap<String, List<WorkFlow>>): Boolean {
    var state = "in"
    while (true) {
        var eval = map[state]!!
        for (work in eval) {
            var applies = true

            if (work.operation == ">") {
                applies = parts[work.type]!! > work.value
            } else if (work.operation == "<") {
                applies = parts[work.type]!! < work.value
            }

            if (applies) {
                if (work.result == "R") return false
                if (work.result == "A") return true
                state = work.result
                break
            }
        }
    }

    throw Exception("shit")
}

class WorkFlow(val operation: String, val type: String, val value: Long, val isOperation: Boolean, val result: String) {
    override fun toString(): String {
        return "WorkFlow(operation='$operation', type='$type', value=$value, isOperation=$isOperation, result='$result')"
    }
}

