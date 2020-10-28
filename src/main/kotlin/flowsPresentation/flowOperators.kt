package flowsPresentation

import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*

suspend fun main() = coroutineScope {
    flowOf(1,2,3,4,5,6,7,8,9,10)
            .map { it * it }
            .filter { it%2 == 0 }
            .takeWhile { it<70 }
            .collect {  println(it)}

    val y = flowOf(1,2,3,4,5,6,7,8,9,10)
            .transform {
                for(i in 1..it){
                    emit("*")
                }
            }
            .toList()
    println(y)
    showZip()
    showMerge()
    showConcat()

}
private suspend fun showZip() {
    val flow1 = flowOf(1,2,3)
    val flow2 = flowOf(4,5,6)
    val zipRes = flow1.zip(flow2){a,b -> a+b}.toList()
    println("zipRes = $zipRes")
}


private suspend fun showMerge() {
    val flow1 = flowOf(1,2,3).onEach { delay(4) }
    val flow2 = flowOf(4,5,6).onEach { delay(3) }
    val mergeRes = merge(flow1, flow2).toList()
    println("mergeRes = $mergeRes")
}

private suspend fun showConcat() {
    val flow1 = flowOf(1,2,3).onEach { delay(4) }
    val flow2 = flowOf(4,5,6).onEach { delay(3) }
    val flattenRes = flowOf(flow1, flow2).flattenConcat().toList()
    println("flattenRes = $flattenRes")
}

