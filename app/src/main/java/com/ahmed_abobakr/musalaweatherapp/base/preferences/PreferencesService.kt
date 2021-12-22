package com.ahmed_abobakr.musalaweatherapp.base.preferences

import android.content.Context
import android.content.SharedPreferences
import android.provider.Settings

object PreferencesService {
     fun getSharedPreferences(context: Context): SharedPreferences{
         val preferences = context.getSharedPreferences("Deceta", Context.MODE_PRIVATE)
         return preferences
     }

}