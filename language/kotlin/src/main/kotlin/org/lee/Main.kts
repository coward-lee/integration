package org.lee

object Main{
    fun main(args: Array<String>) {
        println("Hello world!")
    }
}

/**
 * when 表达式
 */
fun describe(obj: Any): String =
    when (obj) {
        1          -> "One"
        "Hello"    -> "Greeting"
        is Long    -> "Long"
        !is String -> "Not a string"
        else       -> "Unknown"
    }

println(describe("Hello"))
println(describe(1))
println(describe(0L))
println(describe(0.0))
//val item = List("juicy")
//when {
//    "orange" in items -> println("juicy")
//    "apple" in items -> println("apple is fine too")
//}