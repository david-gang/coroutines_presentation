import kotlinx.coroutines.*
import java.io.BufferedReader
import java.io.File
import java.io.FileReader

suspend fun main() = coroutineScope {
    // This is awesome how automatically the launched function is waitedh67
    launch {
        delay(1000)
        println("Kotlin Coroutines World!")
    }
    withTimeout(100){
        resourceManaging(File("/Users/dgang/open-source/coroutines_presentation/src/main/kotlin/coroutines_introduction.kt"))
    }
    println("Hello")
}

private suspend fun resourceManaging(file: File): List<String> {
    println("Don't do this stupid code but it shows how finally is called even nested")
    val reader = BufferedReader(FileReader(file))
    return try {
        return coroutineScope {
             async {
                delay(500)
                val list = mutableListOf<String>()
                var line = reader.readLine()
                while (line != null) {
                    list.add(line)
                    line = reader.readLine();
                }
                list
            }.await()
        }

    }
    finally {
        println("closing")
        reader.close()
    }

}