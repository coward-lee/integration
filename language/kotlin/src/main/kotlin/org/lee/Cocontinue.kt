package org.lee

import kotlinx.coroutines.*

class Cocontinue {

    fun main() {
        GlobalScope.launch { // 在后台启动一个新的协程并继续
            delay(1000L) // 非阻塞的等待 1 秒钟（默认时间单位是毫秒）
            Thread.sleep(1000)
            println("World!") // 在延迟后打印输出
        }
        println("Hello,") // 协程已在等待时主线程还在继续
        Thread.sleep(2000L) // 阻塞主线程 2 秒钟来保证 JVM 存活
    }
}

fun main() {
//    CoroutineScope(Dispatchers.IO).launch {
//        println("携程开始")
//        delay(1000)
//        println("携程开始")
//    }

    println("hello")

    val i = runBlocking (Dispatchers.IO){
        println(Thread.currentThread().name)
        delay(100)
        launch {
            println(Thread.currentThread().name)
        }
        1
    }
    println("结束了吗")
    println("word $i")
}
suspend fun test(){
    println("协程代码块")
    delay(1000)
    println("协程结束")
}