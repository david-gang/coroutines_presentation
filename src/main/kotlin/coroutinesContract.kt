import kotlinx.coroutines.*

val backgroundScope  = CoroutineScope(Dispatchers.Default)
suspend fun main() = coroutineScope {
    badBlockingCode();
    jobsEscapingFromBackground();
    println("Finished")

}

//you are not allowed to block in coroutines
private suspend fun badBlockingCode() {
    Thread.sleep(1000)
}

private suspend fun jobsEscapingFromBackground() {
    backgroundScope.launch {
        println("Don't do this. This job will finish after scope exits")
    }
}