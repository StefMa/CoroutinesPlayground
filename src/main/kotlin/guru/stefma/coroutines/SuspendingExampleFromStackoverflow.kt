package guru.stefma.coroutines

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.launch
import java.util.concurrent.Executors
import kotlin.coroutines.Continuation
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

private var continuation: Continuation<Int>? = null

/**
 * Found here: https://stackoverflow.com/a/48112934/1231245
 */
fun stackoverflowExample() {
    CoroutineScope(Dispatchers.Unconfined).launch {
        println("Launch.. calling a()")
        val a = a()
        println("Result is $a")
    }
    println("Continue after CoroutineScope")
    10.downTo(0).forEach {
        println("ForEach with $it.. call continuation.resume()")
        continuation!!.resume(it)
    }
}

private suspend fun a(): Int {
    return b()
}

private suspend fun b(): Int {
    while (true) {
        println("While called")
        val i = suspendCoroutine<Int> { cont ->
            println("While suspendCoroutine called")
            continuation = cont
        }
        println("While continued")
        if (i == 0) {
            return 0
        }
    }
}
