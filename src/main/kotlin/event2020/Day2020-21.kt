fun main(args: Array<String>) {
    val input = getResourceAsText("input.txt")
    val testInput = getResourceAsText("testInput.txt")

    val splits = input.lines().map { it.split("(contains") }
        .map {
            it.map {
                it.split(Regex(" |\\)|,")).filter { it.isNotEmpty() }.map {
                    it.trim()
                }
            }
        }

    val possibleLinks: MutableMap<String, MutableSet<String>> = mutableMapOf();
    val count: MutableMap<String, Int> = mutableMapOf();

    val ingredients = mutableSetOf<String>()
    val allergens = mutableSetOf<String>()

    for (line in splits) {
        val words = line[0]
        val allergense = line[1]

        ingredients.addAll(words)
        allergens.addAll(allergense)
    }

    for (ingredient in ingredients) {
        possibleLinks[ingredient] = allergens.toMutableSet();
    }


    println(possibleLinks)
    for (line in splits) {
        val words = line[0].toSet()
        val allergense = line[1];

        for (word in words) {
            count.merge(word, 1, Int::plus) // Just count all the words.
        }

        for (allargen in allergense) {
            val difference = ingredients.subtract(words)
            for (ingredient in difference) {
                possibleLinks[ingredient]!!.remove(allargen)
            }
        }

    }

    val sum = possibleLinks
        .filter { it.value.isEmpty() }
        .map { count[it.key]!! }.sum()


    var possibelRelations: MutableMap<String, Set<String>> = possibleLinks
        .filter { it.value.isNotEmpty() }
        .toMutableMap()

    println(possibelRelations)

    while (possibelRelations.filter { it.value.size > 1 }.isNotEmpty()) {
        for (relation in possibelRelations) {
            if (relation.value.size == 1) {
                for (key in possibelRelations.keys) {
                    if (possibelRelations[key]!!.size!! > 1) possibelRelations[key] = possibelRelations[key]!!.subtract(relation.value)
                }
                println(possibelRelations)
            }
        }
    }

    println(possibelRelations.toList()
        .map { Pair(it.first, it.second.first()) }
        .sortedBy { (_, value) -> value }
        .map { it.first }
        .reduce { i, j -> i + "," + j })

}







