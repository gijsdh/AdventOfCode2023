package day24

import getResourceAsText

fun main(args: Array<String>) {
    val input = getResourceAsText("input.txt")
    val inputTest = getResourceAsText("testInput.txt")

    val parsed =
        input.lines().map { it.split("@").map { it.trim() }.map { it.split(",").map { it.trim().toLong() } } }

    println(parsed)

    var list = mutableListOf<Point>()

    for (line in parsed) {
        var x1 = line[0][0]
        var y1 = line[0][1]
        var z1 = line[0][2]
        var Vx = line[1][0]
        var Vy = line[1][1]
        var Vz = line[1][2]

        list.add(Point(x1, y1, z1, Vx, Vy, Vz))
    }


    for (i in 0 until 4) {
        var p = list[i]

        println("(X - ${p.x}) * (${p.Vy} - B) - (Y - ${p.y}) * (${p.Vx} - A) ==0,")
        println("(Y - ${p.y}) * (${p.Vz}  - C) - (Z - ${p.z}) * (${p.Vy} - B) == 0,")

    }
    //{{X->4.72612107765508000000000000000*^14,Y->2.70148844447628000000000000000*^14,Z->2.73604689965980000000000000000*^14
    // Anser = 1016365642179116
    // Solved in Wolfram cloud, there you can solve any number of equation.
    // Learned a lot again, had to revisited high school math / physics.

}




// class Point(val x: Long, val y: Long, val z: Long, val Vx: Long, val Vy: Long, val Vz: Long)
