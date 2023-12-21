import java.lang.Exception
import java.lang.Long.max
import java.lang.Long.min

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
    var rules = mutableMapOf<String, List<WorkFlow>>()

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
                list.add(WorkFlow("", "", 0, false, split[0]))
            }
        }
        rules.put(key, list)
    }
    println(rules)

    var parts = mutableMapOf<String, Pair<Long, Long>>(
        "x" to Pair(1, 4000),
        "m" to Pair(1, 4000),
        "a" to Pair(1, 4000),
        "s" to Pair(1, 4000)
    )

    println(countRanges(parts, rules, "in"))

}

fun countRanges(
    parts: MutableMap<String, Pair<Long, Long>>,
    rules: MutableMap<String, List<WorkFlow>>,
    state: String
): Long {
    if (state == "R") {
        return 0
    } else if (state == "A") {
        var product = 1L
        for (value in parts.values) {
            product *= value.second - value.first + 1
        }
        return product
    }

    var sum = 0L
    var workflows = rules[state]!!

    // qqz{s>2770:qs,m<1801:hdj,R}

    var workMap = parts.toMutableMap()
    for (work in workflows) {
        if (work.isOperation) {
            var range = workMap[work.type]!!
            var T: Pair<Long, Long>
            var F: Pair<Long, Long>

            if (work.operation == "<") {
                T = Pair(range.first, min(work.value - 1, range.second))
                F = Pair(max(range.first, work.value), range.second)
            } else {
                T = Pair(max(range.first, work.value + 1), range.second)
                F = Pair(range.first, min(work.value, range.second))
            }

            if (T.first <= T.second) {
                var copy = workMap.toMutableMap()
                copy[work.type] = T
                sum += countRanges(copy, rules, work.result)
            }

            if (F.first <= F.second) {
                workMap = workMap.toMutableMap()
                workMap[work.type] = F
            }
        } else {
            sum += countRanges(workMap, rules, work.result)
        }
    }

    return sum
}


