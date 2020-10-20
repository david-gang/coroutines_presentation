import java.util.concurrent.CompletableFuture

fun main() {
    println("Starting")
    val tweetsFuture = CompletableFuture.supplyAsync {
        val tweets = getTweets()
        println("tweets are $tweets")
        tweets
    }
    val postsFuture = CompletableFuture.supplyAsync {
        val posts = getPosts()
        println("posts are $posts")
        posts
    }
    val popularityFuture = tweetsFuture.thenCombine(postsFuture) { tweets, posts -> popularity(tweets, posts) }
    val popularityScore = popularityFuture.get()



    println("popularity is $popularityScore")

}