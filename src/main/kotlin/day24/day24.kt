package day24

import getResourceAsText
import java.math.BigDecimal


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


    var sum = 0
    for (i in list.indices) {
        for (j in i + 1 until list.size) {
            var p1 = list[i]
            var p2 = list[j]

            if (p1 == p2) continue

            var x1 = BigDecimal.valueOf(p1.x)
            var x2 = BigDecimal.valueOf(p1.x + p1.Vx)
            var x3 = BigDecimal.valueOf(p2.x)
            var x4 = BigDecimal.valueOf(p2.x + p2.Vx)

            var y1 = BigDecimal.valueOf(p1.y)!!
            var y2 = BigDecimal.valueOf(p1.y + p1.Vy)
            var y3 = BigDecimal.valueOf(p2.y)
            var y4 = BigDecimal.valueOf(p2.y + p2.Vy)

            val divide =
                ((x2 - x1) * (y4 - y3) - (y2 - y1) * (x4 - x3))// ((x1 - x2) * (y3 - y4)) - ((y1 - y2) * (x3 - x4))
            if (divide.signum() != 0) {
                val partOne = (x1 * y2) - (y1 * x2)
                val partTwo = (x3 * y4) - (y3 * x4)

                var Px = (partOne * (x3 - x4) - (x1 - x2) * partTwo) / divide
                var Py = (partOne * (y3 - y4) - (y1 - y2) * partTwo) / divide

                val inPastPointOne = ((Px > x1) == (x2 > x1))
                val inPastPointTwo = ((Px > x3) == (x4 > x3))

                val inPastPointOneY = ((Py > y1) == (y2 > y1))
                val inPastPointTwoY = ((Py > y3) == (y4 > y3))


                val isvalid =
                    Px in BigDecimal.valueOf(200000000000000.0)..BigDecimal.valueOf(400000000000000.0) && Py in BigDecimal.valueOf(
                        200000000000000.0
                    )..BigDecimal.valueOf(400000000000000.0) && inPastPointOne && inPastPointTwo && inPastPointOneY && inPastPointTwoY
                if (isvalid) {
                    sum++
                }
            }
        }
    }
    println(sum)


    for (i in 0 until 4) {
        var p = list[i]

        println("(X - ${p.x}) * (${p.Vy} - B) - (Y - ${p.y}) * (${p.Vx} - A) ==0,")
        println("(Y - ${p.y}) * (${p.Vz}  - C) - (Z - ${p.z}) * (${p.Vy} - B) == 0,")

    }
    // A = velocity rock x direction
    // B = velocity rock y direction
    // C = velocity rock z direction

    // The equation are from:
    // // (xh, yh, zh) -> position hail
    // // (xr, yr, xr) -> position rock
    // // (vx, vy, vz) -> velocity hail
    // // (vxr, vyr, vzr) -> velocity rock
    // (xr - xh) * (vy - vyr) = (yr - yh) * (vx - vxr) = (zr - hz) * (vy - vyr)
    //

    //{{X->4.72612107765508000000000000000*^14,Y->2.70148844447628000000000000000*^14,Z->2.73604689965980000000000000000*^14
    // Anser = 1016365642179116
    // Solved in Wolfram cloud, there you can solve any number of equation.
    // Learned a lot again, had to revisited math / physics.

}

class Point(val x: Long, val y: Long, val z: Long, val Vx: Long, val Vy: Long, val Vz: Long)