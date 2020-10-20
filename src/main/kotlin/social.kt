data class Tweet(val message:String, val retweets:Int)
data class FacebookPost(val message: String, val likes:Int)

fun getPosts():List<FacebookPost> {
    Thread.sleep(1000)
    return listOf(FacebookPost("c",100), FacebookPost("d", 200))
}
fun getTweets():List<Tweet> {
    Thread.sleep(2000)
    return listOf(Tweet("a",100), Tweet("b", 200))
}

fun popularity(tweets: List<Tweet>, posts:List<FacebookPost>):Int{
    return tweets.sumBy { it.retweets } + 10 * posts.sumBy { it.likes }
}