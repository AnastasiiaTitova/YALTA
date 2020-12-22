package com.yalta.repositories

import arrow.core.Either
import okhttp3.Response

sealed class RepoResponse<T>
open class SuccessfulResponse<T> : RepoResponse<T>()
class FailedResponse<T>(val reason: Reason) : RepoResponse<T>()

enum class Reason {
    FAILED_CONNECTION, BAD_CODE, LOGGED_OUT, UNKNOWN
}

inline fun <reified S : SuccessfulResponse<S>, R> process(
    action: () -> RepoResponse<S>,
    onSuccess: (S) -> R,
    onFail: (FailedResponse<S>) -> R
): R {
    return when (val res = action()) {
        is SuccessfulResponse -> {
            onSuccess(res as S)
        }
        is FailedResponse -> {
            onFail(res)
        }
    }
}

fun <T : SuccessfulResponse<T>> Either<Reason, Response>.getRepoResponse(
    successfulResponseBuilder: (Response) -> T
): RepoResponse<T> {
    return when (this) {
        is Either.Left -> {
            FailedResponse(a)
        }
        is Either.Right -> if (b.code() == 200) {
            successfulResponseBuilder(b)
        } else {
            FailedResponse(Reason.BAD_CODE)
        }
    }
}
