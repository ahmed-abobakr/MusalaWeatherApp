package com.ahmed_abobakr.musalaweatherapp.base.di

import com.ahmed_abobakr.musalaweatherapp.base.AppDataManager
import com.ahmed_abobakr.musalaweatherapp.base.BaseViewModel

import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class])
interface AppComponent {


    fun inject(appDataManager: AppDataManager)

    fun inject(viewModel: BaseViewModel)

}