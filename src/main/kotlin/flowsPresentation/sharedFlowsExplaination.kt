package flowsPresentation

import kotlinx.coroutines.*
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

suspend fun main() = coroutineScope {
    simpleSharedFlow()
    replaySharedFlow()
    delay(1000)

}

suspend fun simpleSharedFlow() {
    coroutineScope {
        val sharedFlow = MutableSharedFlow<Int>(replay=0, extraBufferCapacity = 0, onBufferOverflow = BufferOverflow.SUSPEND)
        launch {
            delay(10)
            sharedFlow.emit(1)
            delay(100)
            sharedFlow.emit(2)
        }
        launch {
            sharedFlow.onEach {println("collector a $it") }.launchIn(CoroutineScope(Dispatchers.Default))
            delay(20)
            sharedFlow.onEach {println("collector b $it") }.launchIn(CoroutineScope(Dispatchers.Default))
        }
    }
}


suspend fun replaySharedFlow() {
    coroutineScope {
        val sharedFlow = MutableSharedFlow<Int>(replay=1, extraBufferCapacity = 0, onBufferOverflow = BufferOverflow.SUSPEND)
        launch {
            delay(10)
            sharedFlow.emit(1)
            delay(100)
            sharedFlow.emit(2)
        }
        launch {
            sharedFlow.onEach {println("collector replay a $it") }.launchIn(CoroutineScope(Dispatchers.Default))
            delay(20)
            sharedFlow.onEach {println("collector replay b $it") }.launchIn(CoroutineScope(Dispatchers.Default))
        }
    }
}