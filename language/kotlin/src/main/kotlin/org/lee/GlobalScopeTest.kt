package org.lee

import kotlinx.coroutines.*

class GlobalScopeTest {
}

fun main() {
    /**
     * 官方不推荐使用因为这个不能手动关闭，
     * 推荐使用
     */
//    val globalScope = GlobalScope
//    globalScope.launch {
//        delay(3000)
//        println("hello")
//    }
//    globalScope.launch {
//        delay(3000)
//        println("hello")
//    }

    /**
     * 这种方式可以更加便捷的使用
     */
    var coroutineScope = CoroutineScope(Dispatchers.Default)
    coroutineScope.launch() {
        delay(3000)
        println("hello")
    }
    coroutineScope.launch {
        delay(3000)
        println("hello")
    }
    coroutineScope.cancel()

    var coroutineScope1 = CoroutineScope(Dispatchers.IO)
    coroutineScope1.launch(CoroutineName("demo")) {
        println(coroutineContext[CoroutineName.Key])
        delay(3000)
        println("hello")
    }
    coroutineScope1.launch {
        delay(3000)
        println("hello")
    }
    coroutineScope1.cancel()

    while (true);
}