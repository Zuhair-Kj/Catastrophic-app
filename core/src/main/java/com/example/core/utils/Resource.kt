package com.example.core.utils

data class Resource<out T>(val status: Status, val data: T? = null, val message: String = "") {
    enum class Status {
        SUCCESS,
        ERROR,
        NETWORK_DISCONNECTED,
        LOADING
    }


    fun isError(): Boolean {
        return status == Status.ERROR
    }

    fun isLoading(): Boolean {
        return status == Status.LOADING
    }


    companion object {
        fun <T> success(data: T): Resource<T> {
            return Resource(Status.SUCCESS, data)
        }

        @JvmOverloads
        fun <T> error(data: T? = null, message: String = ""): Resource<T> {
            return Resource(Status.ERROR, data, message)
        }

        fun <T> networkError(data: T? = null): Resource<T> {
            return Resource(Status.NETWORK_DISCONNECTED, data)
        }

        fun <T> loading(): Resource<T> {
            return Resource(Status.LOADING)
        }
    }
}