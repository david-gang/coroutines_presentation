import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.withContext
import okhttp3.*
import java.io.File
import java.io.IOException
import java.nio.file.Files
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine
import kotlin.streams.asSequence


suspend fun main() = coroutineScope {
    println(countWords(File("abc.txt")))
    println(callApi("www.myCoolApi.com"))
}

//create by offloading to thread pool
private suspend fun countWords(file: File) = withContext(Dispatchers.IO){
    Files.lines(file.toPath()).asSequence().map { it.split(" ").size }.sum()
}

//create by integrating with callback
suspend fun callApi(url:String):Response= suspendCoroutine{
    val okHttpClient =  OkHttpClient()
    val request = Request.Builder()
        .url(url)
        .build()
    okHttpClient.newCall(request).enqueue(object:Callback{
        override fun onFailure(call: Call, e: IOException) {
            it.resumeWithException(e)
        }

        override fun onResponse(call: Call, response: Response) {
           it.resume(response)
        }
    })
}

