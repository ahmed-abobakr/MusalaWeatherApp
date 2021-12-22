package com.ahmed_abobakr.musalaweatherapp.base.network



interface ApiManager {

    fun mapException(throwable: Throwable): Throwable

    fun <T> execute(apiCall: () -> T): DefaultResponseModel<T>
}