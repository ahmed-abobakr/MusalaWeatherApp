package com.ahmed_abobakr.musalaweatherapp.base.network



data class DefaultResponseModel<out T>(val status: Status, val data: T? = null, val responseMessage: String? = "", val code: Int = -1)

abstract class BaseResponseModel(open val hasAlert: Boolean,
                               open val alertMessage: String?)

data class BaseResponseModel2(val responseMessage: String = "")


enum class Status {
    SUCCESS, API_EXCEPTION, AUTH_EXCEPTION, NETWORK_EXCEPTION, UNKONWN_EXCEPTION
}