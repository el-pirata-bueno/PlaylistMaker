package com.practicum.playlistmaker.util

sealed interface ErrorType {

    sealed class NetworkError: ErrorType
    object ConnectionError: NetworkError()
    object ServerError: NetworkError()
}