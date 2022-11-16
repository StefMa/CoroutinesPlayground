package guru.stefma.coroutines

fun main() {
    /*
        blockingWithUnconfinedDispatcherBlocksBeforePrintContinued()
        blockingWithAnotherDispatcherPrintContinuedFirst()
        blockingWithSingleThreadDispatcherWillExecuteLaunchSequentially()
        blockingWithTwoThreadDispatcherWillExecuteLaunchParallelInTwoThreads()
        blockingWithSingleThreadDispatcherButLaunchWithMoreThreadsWillExecuteParallelAndSequentially()

        suspendingSingleThreadButOkHttpDifferentThread()
        suspendingTwoThreadsAndOkHttpWithDifferentThread()

        asyncCallsWithSingleThreadAndBlockingHttpCallsWillCallAsyncSequentially()
        asyncCallsWithSingleThreadAndSuspendingHttpCallsWillBeCalledConcurrent()
        asyncCallsTwoThreadsAndBlockingHttpCallWillBeCallFunctionsParallel()
        asyncCallsWithTwoThreadsAndSuspendingHttpCallWillBeCallFunctionsParallelAndConcurrent()

        stackoverflowExample()
    */
    Thread.sleep(3000)
}