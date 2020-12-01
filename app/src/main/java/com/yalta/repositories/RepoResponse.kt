package com.yalta.repositories

sealed class RepoResponse<T>
open class SuccessfulResponse<T> : RepoResponse<T>()
class FailedResponse<T>: RepoResponse<T>()

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
