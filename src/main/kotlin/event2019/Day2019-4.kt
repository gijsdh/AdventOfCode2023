import java.math.BigInteger
import java.security.MessageDigest

fun main(args: Array<String>) {
    val input = "ckczppom"

    var index = 1
    while(true) {
        val key = input + index
        if(md5(key).startsWith("000000")) break
        index++
    }
    println(index)
}

fun md5(input:String): String {
    val md = MessageDigest.getInstance("MD5")
    return BigInteger(1, md.digest(input.toByteArray())).toString(16).padStart(32, '0')
}







