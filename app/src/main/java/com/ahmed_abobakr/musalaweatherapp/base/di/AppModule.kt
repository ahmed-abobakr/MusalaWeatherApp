package com.ahmed_abobakr.musalaweatherapp.base.di

import com.ahmed_abobakr.musalaweatherapp.base.AppDataManager
import com.ahmed_abobakr.musalaweatherapp.base.DataManager
import com.ahmed_abobakr.musalaweatherapp.base.network.MusalaWeatherAPI
import com.ahmed_abobakr.musalaweatherapp.base.network.MusalaWeatherService
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppModule() {

    @Singleton
    @Provides
    fun provideAPI(): MusalaWeatherAPI = MusalaWeatherService.getClient()

    @Provides
    fun providesAppDataManager(): DataManager = AppDataManager()

}