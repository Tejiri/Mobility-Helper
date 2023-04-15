package com.example.mobilityhelper

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatDelegate

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        checkLightOrDarkMode()

        Handler().postDelayed({
            val goToLoginIntent = Intent(this, LoginActivity::class.java)
            startActivity(goToLoginIntent)
            finish()
        }, 2500)
    }

    fun checkLightOrDarkMode() {
        var sharedPreferences: SharedPreferences = getSharedPreferences(
            resources.getString(R.string.sharedPreferencesName), Context.MODE_PRIVATE
        )

        if (sharedPreferences.getBoolean(
                resources.getString(R.string.sharedPreferencesDarkMode), false
            )
        ) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
    }
}