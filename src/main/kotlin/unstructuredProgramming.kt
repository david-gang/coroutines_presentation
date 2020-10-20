import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import java.io.BufferedReader
import java.io.File
import java.io.FileReader
import java.util.concurrent.CompletableFuture
import java.util.concurrent.TimeUnit

fun main() {
    println("Starting")
    val tweetsFuture = badGetTweets()
    val postsFuture = badGetPosts()
    val popularityFuture = tweetsFuture.thenCombine(postsFuture) { tweets, posts -> popularity(tweets, posts) }
    val popularityScore = popularityFuture.get(100, TimeUnit.MILLISECONDS)
    println(popularityScore)
}

fun badGetTweets(): CompletableFuture<List<Tweet>> {
    val future = CompletableFuture.supplyAsync{
        getTweets()
    }
    // this future eescapes
    future.thenAcceptAsync{sendSms(it)}
    return future
}

fun sendSms(tweets: List<Tweet>) {
    println("sending sms")
}

private fun badResourceManaging(file: File):CompletableFuture<List<FacebookPost>> {
    return CompletableFuture.supplyAsync { BufferedReader(FileReader(file)) }.thenApply { reader->
        println("I am going to run but not to finish. NOw file reader stays open")
        Thread.sleep(1000)
        val list = mutableListOf<FacebookPost>()
            var line = reader.readLine()
            while (line != null) {
                list.add(Json.decodeFromString(line))
                line = reader.readLine();
            }
        println("I am dying earlier")
        reader.close()
        list.toList()
    }

}

fun badGetPosts() =badResourceManaging(File("/Users/dgang/open-source/coroutines_presentation/src/main/kotlin/coroutines_introduction.kt"))
