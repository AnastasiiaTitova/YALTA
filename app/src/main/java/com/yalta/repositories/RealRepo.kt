package com.yalta.repositories

import com.yalta.services.SessionService
import okhttp3.*
import ru.gildor.coroutines.okhttp.await

open class RealRepo {
    private val baseUrl = "http://10.0.2.2:9000"
    private val client = OkHttpClient()

    suspend fun doPostRequest(url : String, body : String) : Response {
        val request = Request.Builder()
            .addHeader("Cookie", SessionService.session?.token!!)
            .url("${baseUrl}/${url}")
            .post(RequestBody
                .create(MediaType
                    .parse("text/plain"), body))
            .build()
        return client.newCall(request).await()
    }

    suspend fun doGetRequest(url : String) : Response {
        val request = Request.Builder()
            .addHeader("Cookie", SessionService.session?.token!!)
            .url("${baseUrl}/${url}")
            .build()
        return client.newCall(request).await()
    }

    suspend fun doLoginRequest(login: String, password: String) : Response {
        val url = "${baseUrl}/login?username=$login&password=$password"
        val request = Request.Builder()
            .url(url)
            .post(
                FormBody.Builder()
                    .build()
            )
            .build()
        return client.newCall(request).await()
    }
}
