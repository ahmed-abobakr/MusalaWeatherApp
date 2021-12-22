package com.ahmed_abobakr.musalaweatherapp.base

import com.ahmed_abobakr.musalaweatherapp.base.network.MusalaWeatherAPI
import com.ahmed_abobakr.musalaweatherapp.base.preferences.LocalPreferences

interface DataManager: MusalaWeatherAPI, LocalPreferences { }