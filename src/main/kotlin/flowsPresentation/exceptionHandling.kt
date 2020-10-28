package flowsPresentation

import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import java.lang.Exception

suspend fun main() = coroutineScope{
    //what happened here?
    badExceptionFlow().collect {
        //do you understand what happens here?
        // this is bad because upsttream exception handling is not transparent to downstream
        throw Exception("ouch")
    }

    //what happened here?
    exceptionFlow().collect {
        println("collecting $it")
    }

}

private fun badExceptionFlow() = flow{
    try{
        emit(1)
        emit(2)
        throw Exception("Oy vey")
    }
    catch (e:Exception){
        println ("catched $e")
    }

}

private fun exceptionFlow() = flow{
    emit(1)
    throw Exception("Oy vey")
    //this value is not emitted
    emit(2)
}.catch {  print ("catched $it") }