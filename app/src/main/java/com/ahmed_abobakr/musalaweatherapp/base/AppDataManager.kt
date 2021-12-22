package com.ahmed_abobakr.musalaweatherapp.base


import com.ahmed_abobakr.musalaweatherapp.base.di.DaggerAppComponent
import com.ahmed_abobakr.musalaweatherapp.base.network.MusalaWeatherAPI
import javax.inject.Inject

class AppDataManager : DataManager {

    @Inject
    lateinit var apiService: MusalaWeatherAPI

    init {
        DaggerAppComponent.create().inject(this)
    }

    override fun saveUserToken(userID: String) {

    }

}