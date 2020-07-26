package com.example.weatherapp

import android.content.Context

enum class TempDisplaySetting{
    Fahrenheit, Celcius
}

class TempDisplaySettingManager(context: Context) {
    private val preferences = context.getSharedPreferences("settings", Context.MODE_PRIVATE)

    fun updateSetting(setting: TempDisplaySetting){
        preferences.edit().putString("key_temp_display", setting.name).apply()
    }

    fun getDisplaySetting(): TempDisplaySetting{
       val settingsValue = preferences.getString("key_temp_display", TempDisplaySetting.Fahrenheit.name) ?: TempDisplaySetting.Fahrenheit.name
        return TempDisplaySetting.valueOf(settingsValue)
    }
}