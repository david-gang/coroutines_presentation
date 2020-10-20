import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import java.io.BufferedReader
import java.io.File
import java.io.FileReader
import java.nio.file.Files
import java.util.concurrent.CompletableFuture
import kotlin.streams.asSequence

fun main() {
    println("Starting")
    val tweetsFuture = badGetTweets()
    val postsFuture = badGetPosts()
    val popularityFuture = tweetsFuture.thenCombine(postsFuture) { tweets, posts -> popularity(tweets, posts) }
    val popularityScore = popularityFuture.get()
    println(popularityScore)
}

fun badGetTweets(): CompletableFuture<List<Tweet>> {
    val future = CompletableFuture.supplyAsync{
        getTweets()
    }
    future.thenAcceptAsync{sendSms(it)}
    return future
}

fun sendSms(tweets: List<Tweet>) {
    println("sending sms")
}

private fun badResourceManaging(file: File):CompletableFuture<List<FacebookPost>> {
    BufferedReader(FileReader(file)).use {reader->
        return CompletableFuture.supplyAsync {
            val list = mutableListOf<FacebookPost>()
            var line = reader.readLine()
            while (line != null) {
                list.add(Json.decodeFromString(line))
                line = reader.readLine();
            }
             list
        }
    }

}

fun badGetPosts() =badResourceManaging(File("a.txt"))
