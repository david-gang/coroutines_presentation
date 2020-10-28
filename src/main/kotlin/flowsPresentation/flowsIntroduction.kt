package flowsPresentation

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*

fun main(args:Array<String>) {
    runBlocking {
        explainFlowsAreCold()
    }
    println("flows themselves are just state machines and nott suspending")
    println("we can launch flows in a different scope also")
    createFlow().onEach { println("a $it") }.launchIn(CoroutineScope(Dispatchers.Default))
    createFlow().onEach { println("b $it") }.launchIn(CoroutineScope(Dispatchers.Default))
    runBlocking {
        //what would happen without this delay?
        delay(200)
    }

}
suspend fun explainFlowsAreCold() {
    val flow1 = createFlow()
    println ("flows are cold $flow1")

    //flows are state machines which needs to be kicked in
    println("flows need to be collected")
    //collect is a suspending function which runs in a scope
    createFlow().collect {
        println("collecting $it")
    }
    println("as with all suspending functions we wait till everything finishes")
}

private fun createFlow() = flow{
    for (i in 1..10){
        println("emitting $i")
        emit(i)
        delay(10)
    }
}
