package flowsPresentation

import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*

suspend fun main() = coroutineScope{
    println("by default emitter is suspending till collector receives stuff")
    createFlow().collect {
        println("slow collecting $it")
        delay(20)
    }

    println("we can buffer the results for relieving the emitter")
    createFlow().buffer(3).collect {
        println("slow collecting $it")
        delay(20)
    }

    println("we can conflate (skip) new values for slow collectors")
    createFlow().conflate().collect {
        println("slow collecting $it")
        delay(19)
    }


}


private fun createFlow() = flow{
    for (i in 1..5){
        println("emitting $i")
        emit(i)
        delay(10)
    }
}