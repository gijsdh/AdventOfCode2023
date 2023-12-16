import java.lang.Exception

fun main(args: Array<String>) {
    val input = getResourceAsText("input.txt")
    val inputTest = getResourceAsText("testInput.txt")

    val parsed = input.lines()[0].split(",").map { it.trim() }

    var sum = 0

    var lenses: List<MutableMap<String, Int>> = List(256) { mutableMapOf() }

    for (line in parsed) {
        val splited = line.split(Regex("=|-"))
        var label = splited[0]
        var labelNumer = 0
        for (char in label) {
            var code = char.code
            labelNumer += code
            labelNumer *= 17
            labelNumer %= 256
        }

        if (line.contains('=')) {
            var focal = splited[1].toInt()
            val box = lenses[labelNumer]
            box[label] = focal
        } else if (line.contains('-')) {
            val box = lenses[labelNumer]
            box.remove(label)
        } else {
            throw Exception("shit")
        }
    }


    for ((i, lense) in lenses.withIndex()) {
        var index = 1
        for (entry in lense) {
            sum += (i + 1) * (index * entry.value)
            index++
        }
    }


    println(lenses)

    println(sum)
}