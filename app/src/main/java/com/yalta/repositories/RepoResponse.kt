package com.yalta.repositories

sealed class RepoResponse
open class SuccessfulResponse : RepoResponse()
class FailedResponse: RepoResponse()

inline fun <reified S : SuccessfulResponse, R> process(
    action: () -> RepoResponse,
    onSuccess: (S) -> R,
    onFail: (FailedResponse) -> R
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
