package guru.stefma.coroutines

import guru.stefma.coroutines.http.blockingHttpCallWithLogging
import guru.stefma.coroutines.http.suspendingHttpCallWithLogging
import kotlinx.coroutines.*
import java.util.concurrent.Executors

fun asyncCallsWithSingleThreadAndBlockingHttpCallsWillCallAsyncSequentially() {
    val fixedThreadDispatcher = Executors.newFixedThreadPool(1).asCoroutineDispatcher()
    CoroutineScope(fixedThreadDispatcher).launch {
        println("Initial launch call: ${Thread.currentThread().name}")
        println("0 - Start")

        val first = async {
            println("1 + ${Thread.currentThread().name}")
            blockingHttpCallWithLogging("1")
            println("1 Result")
        }

        val second = async {
            println("2 + ${Thread.currentThread().name}")
            blockingHttpCallWithLogging("2")
            println("2 Result")
        }

        println("0 - End Before Await")
        listOf(first, second).awaitAll()
        println("0 - End After Await")
    }
}

fun asyncCallsWithSingleThreadAndSuspendingHttpCallsWillBeCalledConcurrent() {
    val fixedThreadDispatcher = Executors.newFixedThreadPool(1).asCoroutineDispatcher()
    CoroutineScope(fixedThreadDispatcher).launch {
        println("Initial launch call: ${Thread.currentThread().name}")
        println("0 - Start")

        val first = async {
            println("1 + ${Thread.currentThread().name}")
            suspendingHttpCallWithLogging("1")
            println("1 END")
        }

        val second = async {
            println("2 + ${Thread.currentThread().name}")
            suspendingHttpCallWithLogging("2")
            println("2 Result")
        }

        println("0 - End Before Await")
        listOf(first, second).awaitAll()
        println("0 - End After Await")
    }
}

fun asyncCallsTwoThreadsAndBlockingHttpCallWillBeCallFunctionsParallel() {
    val fixedThreadDispatcher = Executors.newFixedThreadPool(2).asCoroutineDispatcher()
    CoroutineScope(fixedThreadDispatcher).launch {
        println("Initial launch call: ${Thread.currentThread().name}")
        println("0 - Start")

        val first = async {
            println("1 + ${Thread.currentThread().name}")
            blockingHttpCallWithLogging("1")
            println("1 Result")
        }

        val second = async {
            println("2 + ${Thread.currentThread().name}")
            blockingHttpCallWithLogging("2")
            println("2 Result")
        }

        println("0 - End Before Await")
        listOf(first, second).awaitAll()
        println("0 - End After Await")
    }
}

fun asyncCallsWithTwoThreadsAndSuspendingHttpCallWillBeCallFunctionsParallelAndConcurrent() {

    // This is quite interesting.
    // Depending on how many threads you put into the pool, the
    // output of the statement
    // "0 - End Before Await"
    // will be printed at another point.
    // If we have only two threads, it will be printed as a **fourth** statement.
    // In case we have three threads, as a **fifth** statement.
    // Four threads, **six** statement and so on...
    val fixedThreadDispatcher = Executors.newFixedThreadPool(2).asCoroutineDispatcher()
    CoroutineScope(fixedThreadDispatcher).launch {
        println("Initial launch call: ${Thread.currentThread().name}")
        println("0 - Start")

        var list: List<Deferred<Unit>> = emptyList()
        repeat(8) { index ->
            val indexPlusOne = index + 1
            list = buildList {
                add(
                    async {
                        println("$indexPlusOne + ${Thread.currentThread().name}")
                        suspendingHttpCallWithLogging(indexPlusOne.toString())
                        println("$indexPlusOne Result")
                    }
                )
            }
        }

        println("0 - End Before Await")
        list.awaitAll()
        println("0 - End After Await")
    }
}