package guru.stefma.coroutines.http

import okhttp3.*
import java.io.IOException
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

fun blockingHttpCallWithLogging(logTag: String) {
    val startTime = System.currentTimeMillis()

    val response = blockingHttpCall()
    println("$logTag: Statuscode -> $response")

    val endTime = System.currentTimeMillis()
    println("$logTag: Time for HTTP executing: ${endTime - startTime} milliseconds")
}

/**
 * This will do the http call **by blocking the current thread**.
 * But it doesn't - for testing purpose - call `Continuation.resume()`.
 */
private fun blockingHttpCall(): String {
    val httpClient = OkHttpClient()

    val request = Request.Builder()
        .url("https://http.cat/100")
        .build()

    val response = httpClient.newCall(request).execute()
    return "${response.code}"
}

suspend fun suspendingHttpCallWithLogging(logTag: String) {
    val startTime = System.currentTimeMillis()

    val response = suspendingHttpCall()
    println("$logTag: Statuscode -> $response")

    val endTime = System.currentTimeMillis()
    println("$logTag Time for HTTP executing: ${endTime - startTime} milliseconds")
}

/**
 * This will do the http call **without blocking the current thread**.
 */
private suspend fun suspendingHttpCall(): String = suspendCoroutine { continuation ->
    val httpClient = OkHttpClient()

    val request = Request.Builder()
        .url("https://http.cat/100")
        .build()

    httpClient.newCall(request).enqueue(object : Callback {
        override fun onFailure(call: Call, e: IOException) {
            continuation.resumeWithException(e)
        }

        override fun onResponse(call: Call, response: Response) {
            continuation.resume("${response.code}")
        }

    })
}

/**
 * This will do the http call **without blocking the current thread**.
 * But it **doesn't** - for testing purpose - **call `Continuation.resume()`**.
 */
private suspend fun suspendingHttpCallWithoutResume(): String = suspendCoroutine { _ ->
    val httpClient = OkHttpClient()

    val request = Request.Builder()
        .url("https://http.cat/100")
        .build()

    val response = httpClient.newCall(request).execute()
    //continuation.resume(response.code.toString())
}