package guru.stefma.coroutines

import guru.stefma.coroutines.http.blockingHttpCallWithLogging
import kotlinx.coroutines.*
import java.util.concurrent.Executors

@OptIn(ExperimentalStdlibApi::class)
fun blockingWithUnconfinedDispatcherBlocksBeforePrintContinued() {
    blockingHttpCallWithLogging("1")

    // When execute on a different Dispatchers (== another thread)
    // The next println will be called "directly"
    // after we started the coroutineScope here.
    // In case the Dispatcher is Unconfined (probably called from main), it will "block"
    // the current thread and this functions continue only when this will return
    CoroutineScope(Dispatchers.Unconfined).launch {
        println("Execute on ${this.coroutineContext[CoroutineDispatcher.Key]}")
        blockingHttpCallWithLogging("2")
    }

    println("Continued?!")
}

@OptIn(ExperimentalStdlibApi::class)
fun blockingWithAnotherDispatcherPrintContinuedFirst() {
    blockingHttpCallWithLogging("1")

    // When execute on a different Dispatchers (== another thread)
    // The next println will be called "directly"
    // after we started the coroutineScope here.
    // In case the Dispatcher is Unconfined (probably called from main), it will "block"
    // the current thread and this functions continue only when this will return
    CoroutineScope(Dispatchers.IO).launch {
        println("Execute on ${this.coroutineContext[CoroutineDispatcher.Key]}")
        blockingHttpCallWithLogging("2")
    }

    println("Continued?!")
}

fun blockingWithSingleThreadDispatcherWillExecuteLaunchSequentially() {
    // Depending on the dispatcher, each launch function inside this scope
    // could run in itw own THREAD!  (and therefore parallel!!)
    // E.g. in case we use `Executors.newSingleThreadExecutors()`, each launch will run sequential
    // When we use a Fixed Thread Pool size of 2, two launch functions will run parallel, the other two
    // has to wait until one of the other Threads are free again.
    // Launch is a coroutine builder function
    val fixedThreadDispatcher = Executors.newFixedThreadPool(1).asCoroutineDispatcher()
    CoroutineScope(fixedThreadDispatcher).launch {
        println("Initial launch call: ${Thread.currentThread().name}")
        println("0 - Start")

        println("1")
        launch {
            println("2 + ${Thread.currentThread().name}")
            blockingHttpCallWithLogging("2")
            println("2 Result")
        }

        println("3")
        launch {
            println("4 + ${Thread.currentThread().name}")
            blockingHttpCallWithLogging("4")
            println("4 Result")
        }

        println("5")
        launch {
            println("6 + ${Thread.currentThread().name}")
            blockingHttpCallWithLogging("6")
            println("6 Result")
        }

        println("7")
        blockingHttpCallWithLogging("7")
        println("7 - End")
    }
}

fun blockingWithSingleThreadDispatcherButLaunchWithMoreThreadsWillExecuteParallelAndSequentially() {
    val fixedThreadDispatcher = Executors.newFixedThreadPool(1).asCoroutineDispatcher()
    val fixedThreadDispatcherForLaunchCalls = Executors.newFixedThreadPool(2).asCoroutineDispatcher()
    CoroutineScope(fixedThreadDispatcher).launch {
        println("Initial launch call: ${Thread.currentThread().name}")
        println("0 - Start")

        println("1")
        launch(fixedThreadDispatcherForLaunchCalls) {
            println("2 + ${Thread.currentThread().name}")
            blockingHttpCallWithLogging("2")
            println("2 Result")
        }

        println("3")
        launch {
            println("4 + ${Thread.currentThread().name}")
            blockingHttpCallWithLogging("4")
            println("4 Result")
        }

        println("5")
        launch(fixedThreadDispatcherForLaunchCalls) {
            println("6 + ${Thread.currentThread().name}")
            blockingHttpCallWithLogging("6")
            println("6 Result")
        }

        println("7")
        blockingHttpCallWithLogging("7")
        println("7 - End")
    }
}

fun ss() {
    val fixedThreadDispatcher = Executors.newFixedThreadPool(1).asCoroutineDispatcher()
    CoroutineScope(fixedThreadDispatcher).launch {
        println("Initial launch call: ${Thread.currentThread().name}")
        println("0 - Start")

        println("1")
        launch(Dispatchers.IO) {
            println("2 + ${Thread.currentThread().name}")
            blockingHttpCallWithLogging("2")
            println("2 Result")
        }

        println("3")
        launch {
            println("4 + ${Thread.currentThread().name}")
            blockingHttpCallWithLogging("4")
            println("4 Result")
        }

        println("5")
        launch(Dispatchers.IO) {
            println("6 + ${Thread.currentThread().name}")
            blockingHttpCallWithLogging("6")
            println("6 Result")
        }

        println("7")
        blockingHttpCallWithLogging("7")
        println("7 - End")
    }
}