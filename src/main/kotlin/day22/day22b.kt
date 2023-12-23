package day22

import getResourceAsText



fun main(args: Array<String>) {
    val input = getResourceAsText("input.txt")
    val inputTest = getResourceAsText("testInput.txt")

    val parsed = input.lines().map { it.split("~").map { it.split(",") } }
    println(parsed)


    var objects = mutableSetOf<MutableSet<Triple<Int, Int, Int>>>()
    for (line in parsed) {
        var edgeOne = toTriple(line[0])
        var edgeTwo = toTriple(line[1])
        var obj = mutableSetOf<Triple<Int, Int, Int>>()
        if (edgeOne.first != edgeTwo.first) {
            for (i in edgeOne.first until edgeTwo.first + 1) {
                obj.add(Triple(i, edgeOne.second, edgeOne.third))
            }
        } else if (edgeOne.second != edgeTwo.second) {
            for (i in edgeOne.second until edgeTwo.second + 1) {
                obj.add(Triple(edgeOne.first, i, edgeOne.third))
            }
        } else if (edgeOne.third != edgeTwo.third) {
            for (i in edgeOne.third until edgeTwo.third + 1) {
                obj.add(Triple(edgeOne.first, edgeOne.second, i))
            }
        } else if (edgeOne == edgeTwo) {
            obj.add(Triple(edgeOne.first, edgeOne.second, edgeOne.third))
        } else {
            throw Exception("shit")
        }

        objects.add(obj)
    }


    var stable = mutableSetOf<Triple<Int, Int, Int>>()
    stable.addAll(objects.flatten())


    while (true) {
        var workSet = mutableSetOf<MutableSet<Triple<Int, Int, Int>>>()
        var stop = true
        for ((i, obj) in objects.withIndex()) {
            var moveDown = true
            for (cube in obj) {
                if (cube.third == 1) moveDown = false
                val shiftDown = Triple(cube.first, cube.second, cube.third - 1)
                if (stable.contains(shiftDown) && !obj.contains(shiftDown)) moveDown = false
            }
            if (moveDown) {
                stop = false
                var temp = mutableSetOf<Triple<Int, Int, Int>>()
                for (cube in obj) {
                    stable.remove(Triple(cube.first, cube.second, cube.third))
                    val element = Triple(cube.first, cube.second, cube.third - 1)

                    stable.add(element)
                    temp.add(element)
                }
                workSet.add(temp)
            } else {
                workSet.add(obj)
            }
        }
        objects = workSet.toMutableSet()
        if (stop) break
    }

    var sum =0
    println(objects)

    for ((k, objOne) in objects.withIndex()) {
        var copyObject = objects.toMutableList()
        var work = stable.toMutableSet()
        work.removeAll(objOne)

        val falling = mutableSetOf<Int>()
        while (true) {
            var stop = true
            for ((i, obj) in copyObject.withIndex()) {
                if (obj == objOne) continue
                var moveDown = true
                for (cube in obj) {
                    if (cube.third == 1) moveDown = false
                    val shiftDown = Triple(cube.first, cube.second, cube.third - 1)
                    if (work.contains(shiftDown) && !obj.contains(shiftDown)) moveDown = false
                }
                if (moveDown) {
                    falling.add(i)
                    stop = false
                    var temp = mutableSetOf<Triple<Int, Int, Int>>()
                    for (cube in obj) {

                        work.remove(Triple(cube.first, cube.second, cube.third))
                        val element = Triple(cube.first, cube.second, cube.third - 1)

                        work.add(element)
                        temp.add(element)
                    }
                    copyObject[i] = temp
                }
            }
            if (stop) break
        }
        sum += falling.size
    }
    println(sum)
}
