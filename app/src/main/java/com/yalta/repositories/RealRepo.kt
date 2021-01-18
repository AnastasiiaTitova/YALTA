package com.yalta.repositories

import arrow.core.Either
import com.yalta.services.SessionService
import okhttp3.*
import ru.gildor.coroutines.okhttp.await
import java.net.ConnectException
import java.net.SocketTimeoutException

open class RealRepo {
    private val baseUrl = "http://10.0.2.2:9000"
    private val client = OkHttpClient()

    suspend fun doPostRequest(url : String, body : String) : Either<Reason, Response> {
        val token = SessionService.session?.token ?: return Either.Left(Reason.LOGGED_OUT)
        val request = Request.Builder()
            .addHeader("Cookie", token)
            .url("${baseUrl}/${url}")
            .post(RequestBody
                .create(MediaType
                    .parse("text/plain"), body))
            .build()
        return try {
            Either.Right(client.newCall(request).await())
        } catch (e: Exception) {
            Either.Left(when (e) {
                is SocketTimeoutException, is ConnectException -> Reason.FAILED_CONNECTION
                else -> Reason.UNKNOWN
            })
        }
    }

    suspend fun doGetRequest(url: String, queryParameters: List<Pair<String, String>> = emptyList()): Either<Reason, Response> {
        val token = SessionService.session?.token ?: return Either.Left(Reason.LOGGED_OUT)

        val httpUrl = HttpUrl.parse("${baseUrl}/${url}")?.newBuilder()
        for (parameter in queryParameters) {
            httpUrl?.addQueryParameter(parameter.first, parameter.second)
        }
        httpUrl?.build()

        val request = Request.Builder()
            .addHeader("Cookie", token)
            .url(httpUrl.toString())
            .build()
        return try {
            Either.Right(client.newCall(request).await())
        } catch (e: Exception) {
            Either.Left(when (e) {
                is SocketTimeoutException, is ConnectException -> Reason.FAILED_CONNECTION
                else -> Reason.UNKNOWN
            })
        }
    }

    suspend fun doPutRequest(url : String, body : String): Either<Reason, Response> {
        val token = SessionService.session?.token ?: return Either.Left(Reason.LOGGED_OUT)
        val request = Request.Builder()
            .addHeader("Cookie", token)
            .url("${baseUrl}/${url}")
            .put(RequestBody
                .create(MediaType
                    .parse("text/plain"), body))
            .build()
        return try {
            Either.Right(client.newCall(request).await())
        } catch (e: Exception) {
            Either.Left(when (e) {
                is SocketTimeoutException, is ConnectException -> Reason.FAILED_CONNECTION
                else -> Reason.UNKNOWN
            })
        }
    }

    suspend fun doLoginRequest(login: String, password: String): Either<Reason, Response> {
        val url = "${baseUrl}/login?username=$login&password=$password"
        val request = Request.Builder()
            .url(url)
            .post(
                FormBody.Builder()
                    .build()
            )
            .build()
        return try {
            Either.Right(client.newCall(request).await())
        } catch (e: Exception) {
            Either.Left(when (e) {
                is SocketTimeoutException, is ConnectException -> Reason.FAILED_CONNECTION
                else -> Reason.UNKNOWN
            })
        }
    }
}
