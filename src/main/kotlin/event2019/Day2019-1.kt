fun main(args: Array<String>) {
    val input = getResourceAsText("input.txt")

    val splits = input.split("").map { it.trim() }.filter { it.isNotEmpty() }
    println(splits.count { it == "(" } - splits.count { it == ")" })

    var index = 0
    var i = 0
    while (index > -1) {
        println(splits[i])
        if (splits[i] == "(") index++
        else index--
        i++
    }

    println(i)
}







