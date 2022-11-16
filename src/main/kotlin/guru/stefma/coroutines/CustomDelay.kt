package guru.stefma.coroutines

import kotlin.concurrent.thread
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

/**
 * This demonstrates how the original `delay` function work.
 *
 * It doesn't block the current thread and resume after 2600 milliseconds.
 */
suspend fun customDelay() = suspendCoroutine {
    thread {
        Thread.sleep(2600)
        it.resume(Unit)
    }
}