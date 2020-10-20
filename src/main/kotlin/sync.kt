//what is the problem?
//what is good?
fun main() {
    println("Starting")
    val tweets = getTweets()
    println("tweets are $tweets")
    val posts = getPosts()
    println("posts are $posts")
    println("popularity is ${popularity(tweets, posts)}")
}



