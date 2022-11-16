package guru.stefma.coroutines

import guru.stefma.coroutines.http.suspendingHttpCallWithLogging
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.launch
import java.util.concurrent.Executors

fun suspendingSingleThreadButOkHttpDifferentThreadWillCallConcurrent() {
    // In this example we put only one Thread into the CoroutineScope.
    // This means it will run on only ONE thread.
    // But, the suspendingExample() function moves their operation
    // to another thread (by using OkHttp for this, which does this for us).
    // With that, the single thread doesn't have to wait UNTIL the first launch is done.
    // It can simply pause (suspend!) the first launch function, and hop to the next.
    // There we have the same code, it will execute the suspendingHttpCallWithLogging, this moves their tasks
    // to another thread, the ONE thread is idle can can do "other things"... Maybe resuming
    // the first launch call :upside:...
    // It looks it is ASYNC, but it isn't! It is concurrent. The single thread just hops their tasks back and forth...
    //
    // IF we suspendingExample to blocking, the launch calls will be executed sequentially
    // IF we add more threads, doesn't mind what the suspendingExample does, the launch calls will be executed parallel!
    val fixedThreadDispatcher = Executors.newFixedThreadPool(1).asCoroutineDispatcher()
    CoroutineScope(fixedThreadDispatcher).launch {
        println("Initial launch call: ${Thread.currentThread().name}")
        println("0 - Start")

        println("1")
        launch {
            println("2 + ${Thread.currentThread().name}")
            suspendingHttpCallWithLogging("2")
            println("2 Result")
        }

        println("3")
        launch {
            println("4 + ${Thread.currentThread().name}")
            suspendingHttpCallWithLogging("4")
            println("4 Result")
        }

        println("5")
        launch {
            println("6 + ${Thread.currentThread().name}")
            suspendingHttpCallWithLogging("6")
            println("6 Result")
        }

        println("7")
        suspendingHttpCallWithLogging("7")
        println("7 - End")
    }

    println("Done")
}

fun suspendingTwoThreadsAndOkHttpWithDifferentThreadWillCallParallelAndConcurrent() {
    // In this example we put only one Thread into the CoroutineScope.
    // This means it will run on only ONE thread.
    // But, the suspendingExample() function moves their operation
    // to another thread (by using OkHttp for this, which does this for us).
    // With that, the single thread doesn't have to wait UNTIL the first launch is done.
    // It can simply pause (suspend!) the first launch function, and hop to the next.
    // There we have the same code, it will execute the suspendingHttpCallWithLogging, this moves their tasks
    // to another thread, the ONE thread is idle can can do "other things"... Maybe resuming
    // the first launch call :upside:...
    // It looks it is ASYNC, but it isn't! It is concurrent. The single thread just hops their tasks back and forth...
    //
    // IF we suspendingHttpCallWithLogging to blocking, the launch calls will be executed sequentially
    // IF we add more threads, doesn't mind what the suspendingHttpCallWithLogging does, the launch calls will be executed parallel!
    // In the later case, it will be executed in parallel and concurrent (two threads doing tasks hopping)
    val fixedThreadDispatcher = Executors.newFixedThreadPool(2).asCoroutineDispatcher()
    CoroutineScope(fixedThreadDispatcher).launch {
        println("Initial launch call: ${Thread.currentThread().name}")
        println("0 - Start")

        println("1")
        launch {
            println("2 + ${Thread.currentThread().name}")
            suspendingHttpCallWithLogging("2")
            println("2 Result")
        }

        println("3")
        launch {
            println("4 + ${Thread.currentThread().name}")
            suspendingHttpCallWithLogging("4")
            println("4 Result")
        }

        println("5")
        launch {
            println("6 + ${Thread.currentThread().name}")
            suspendingHttpCallWithLogging("6")
            println("6 Result")
        }

        println("7")
        suspendingHttpCallWithLogging("7")
        println("7 - End")
    }

    println("Done")
}