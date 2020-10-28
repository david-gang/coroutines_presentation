import kotlinx.coroutines.*

suspend fun main() = coroutineScope {
    exceptionsAreCatchedNormally()
    try {
        otherJobsInScopeAreCanceledAutomatically()
    }
    catch (e:Exception){
        println("I can catch Exceptions upstream")
    }

    supervisorJobRunsAllCoroutinesEvenAfterFailing()
    Unit


}

suspend fun supervisorJobRunsAllCoroutinesEvenAfterFailing() =supervisorScope {
        launch {
            failingFun()
        }
        launch {
            println("Starting coroutine which will finish")
            delay(200)
            println("Now i am going to be printed")
        }
    }



suspend fun otherJobsInScopeAreCanceledAutomatically() = coroutineScope {
        launch {
            failingFun()
        }
        launch {
            println("Starting coroutine which won't finissh")
            delay(200)
            println("This is fun. You won't see this print because i am already cancelled")
        }
    }


suspend fun exceptionsAreCatchedNormally() {
    try {
        failingFun()
    }
    catch (e: Exception){
        println("there is nothing fancy in catching exceptions. No esoteric catch operators!. Got $e")
    }
}

suspend fun failingFun() {
    delay(1)
    throw Exception("Oy Vey")
}