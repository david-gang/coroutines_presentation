//Adopted from https://kotlinlang.org/docs/reference/coroutines/shared-mutable-state-and-concurrency.html
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.actor
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import java.util.concurrent.atomic.AtomicInteger

suspend fun main() = coroutineScope {
    coroutinesDontSolveSharedState()
    sharedStateClassicalProgramming()
    sharedStateAssignToContext()
    sharedStateMutex()
    sharedStateUseActor()
}

//This is a non blocking lock!!!
suspend fun sharedStateMutex(){
    val mutex = Mutex()
    var counter = 0
    withContext(Dispatchers.Default) {
        for(i in 1..10000) {
            launch {
                mutex.withLock {
                    counter++
                }
            }

        }
    }
    println("sharedStateMutex Counter = $counter")
}

suspend fun sharedStateAssignToContext(){
    val context =  newSingleThreadContext("abc")
    var counter = 0
    withContext(context) {
        for(i in 1..10000) {
            launch {
                counter++
            }

        }
    }
    println("sharedStateAssignToContext Counter = $counter")
    context.close()
}


suspend fun sharedStateClassicalProgramming(){
    var counter = AtomicInteger(0)
    withContext(Dispatchers.Default) {
        for(i in 1..10000) {
            launch {
                counter.incrementAndGet()
            }

        }
    }
    println("sharedStateClassicalProgramming Counter = $counter")
}



suspend fun coroutinesDontSolveSharedState(){
    var counter = 0
    withContext(Dispatchers.Default) {
        for(i in 1..10000) {
            launch {
                counter++
            }

        }
    }
    println("coroutinesDontSolveSharedState Counter = $counter")
}

// Message types for counterActor
sealed class CounterMsg
object IncCounter : CounterMsg() // one-way message to increment counter
class GetCounter(val response: CompletableDeferred<Int>) : CounterMsg() // a request with reply

// This function launches a new counter actor
fun CoroutineScope.counterActor() = actor<CounterMsg> {
    var counter = 0 // actor state
    for (msg in channel) { // iterate over incoming messages
        when (msg) {
            is IncCounter -> counter++
            is GetCounter -> msg.response.complete(counter)
        }
    }
}

suspend fun sharedStateUseActor(){
    coroutineScope {
        val counter = counterActor() // create the actor
        withContext(Dispatchers.Default) {
            for(i in 1..10000) {
                launch {
                    counter.send(IncCounter)
                }
            }
        }
        // send a message to get a counter value from an actor
        val response = CompletableDeferred<Int>()
        counter.send(GetCounter(response))
        println("sharedStateUseActor Counter = ${response.await()}")
        counter.close() // shutdown the actor
    }

}