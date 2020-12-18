package com.yalta.repositories

import okhttp3.Response

sealed class RepoResponse<T>
open class SuccessfulResponse<T> : RepoResponse<T>()
class FailedResponse<T>(val reason: Reason) : RepoResponse<T>()

enum class Reason {
    FAILED_CONNECTION, BAD_CODE
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

fun <T : SuccessfulResponse<T>> Response?.getRepoResponse(
    successfulResponseBuilder: (Response) -> T
): RepoResponse<T> {
    return when {
        this == null -> {
            FailedResponse(Reason.FAILED_CONNECTION)
        }
        this.code() != 200 -> {
            FailedResponse(Reason.BAD_CODE)
        }
        else -> {
            successfulResponseBuilder(this)
        }
    }
}
