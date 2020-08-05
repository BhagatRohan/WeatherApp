package com.example.weatherapp.utils

import android.content.Context
import android.widget.Toast
import androidx.appcompat.app.AlertDialog

fun formatTemperature(temperature: Float, tempDisplaySetting: TempDisplaySetting): String {

   return when(tempDisplaySetting){
       TempDisplaySetting.Fahrenheit -> String.format("%.2f C°", temperature)
       TempDisplaySetting.Celsius ->{
           val temp = (temperature - 32f) * (5f/9f)
           String.format("%.2f C°", temp)
       }
   }
}

fun showTempDisplaySettingDialog(context: Context, tempDisplaySettingManager: TempDisplaySettingManager) {
    val dialogBuilder = AlertDialog.Builder(context)
        .setTitle("Choose Display Units")
        .setMessage("Choose which Temperature unit to use for temperature display")
        .setPositiveButton("F°") { _, _ ->
            tempDisplaySettingManager.updateSetting(TempDisplaySetting.Fahrenheit)
        }
        .setNeutralButton("C°") { _, _ ->
            tempDisplaySettingManager.updateSetting(TempDisplaySetting.Celsius)
        }
        .setOnDismissListener {
            Toast.makeText(context, "Settings will take change on app restart", Toast.LENGTH_SHORT)
                .show()
        }
    dialogBuilder.show()
}
