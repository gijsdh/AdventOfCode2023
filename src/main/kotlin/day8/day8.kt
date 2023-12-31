import java.lang.Exception

fun main(args: Array<String>) {
    val input = getResourceAsText("input.txt")
    val parsed = input.lines()
    val commands = parsed[0]

    val mapping = parsed.drop(2)
        .map { it.split(Regex(" = \\(|, |\\)"))
            .filter { it.isNotEmpty() } }
    val map: MutableMap<String, Pair<String, String>> = mutableMapOf();
    for (line in mapping) {
        map[line[0]] = Pair(line[1], line[2])
    }

    // Part one
    println(findSumPath(commands, map, Pair("AAA", 0)).second)

    val starts = map.keys.filter { it.last() == 'A' }
    println(starts)
    val list = mutableListOf<Pair<String, Int>>()
    starts.forEach { list.add(Pair(it, 0)) }
    var index = 0

    // This all works as Length(XXA -> XXZ) == Length(XXZ-> XXZ). Which is odd, but apparently a property of the input.
    while (index < 4) {
        for (i in starts.indices) {
            list[i] = findSumPath(commands, map, list[i])
        }
        println(list)
        index++
    }

    // The looping takes to long, but when we print all list we see that paths through the maze Loop at constant intervals
    // So we can find the smallest value where all the intervals intersects.
    // We need to find the Least common multiplier, we can use the following formula for that
    // LCM(n1, n2) = (n1 * n2) / GCD(n1, n2) and LCM(n1, n2, n3) = (n3*(n1 * n2) / GCD(n1, n2))/GCD(n1*n2, n3)) ..
    // GCD is the greatest common factor
    var sum2 = 1L
    for (line in list) {
        sum2 = (line.second * sum2) / gcd(sum2, line.second.toLong())
    }

    println(sum2 / index)
    println(lcm(list.map { it.second.toLong() }) / index)
}

private fun findSumPath(
    commands: String,
    map: MutableMap<String, Pair<String, String>>,
    triple: Pair<String, Int>
): Pair<String,Int> {
    var pos = triple.first
    var index = triple.second
    while (true) {
        var command = commands[index % commands.length]
        pos = when(command) {
            'R' -> map[pos]!!.second
            'L' -> map[pos]!!.first
            else -> throw Exception("shit")
        }
        index++
        if (pos.last() == 'Z' || pos == "ZZZ") break
    }
    return Pair(pos, index)
}

// https://rosettacode.org/wiki/Greatest_common_divisor#Kotlin
// The greatest common divisor or greatest common factor (gcf)
tailrec fun gcd(a: Long, b: Long): Long = if (b == 0L) kotlin.math.abs(a) else gcd(b, a % b)
tailrec fun lcm(a: List<Long>): Long = a.reduce { first, second -> first * second / gcd(first, second) }





