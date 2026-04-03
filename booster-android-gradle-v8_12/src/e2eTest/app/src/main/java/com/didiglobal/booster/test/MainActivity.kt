package com.didiglobal.booster.test

import android.app.Activity
import android.os.Bundle
import android.preference.PreferenceManager

class MainActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Exercise Context.getSharedPreferences()
        // Transformed to: ShadowSharedPreferences.getSharedPreferences(context, name, mode)
        val prefs1 = getSharedPreferences("test_prefs", MODE_PRIVATE)
        prefs1.edit().putString("key1", "value1").apply()

        // Exercise PreferenceManager.getDefaultSharedPreferences()
        // Transformed to: ShadowSharedPreferences.getDefaultSharedPreferences(context)
        @Suppress("DEPRECATION")
        val prefs2 = PreferenceManager.getDefaultSharedPreferences(this)
        prefs2.edit().putInt("key2", 42).apply()

        // Exercise Activity.getPreferences()
        // Transformed to: ShadowSharedPreferences.getPreferences(activity, mode)
        val prefs3 = getPreferences(MODE_PRIVATE)
        prefs3.edit().putBoolean("key3", true).apply()
    }
}
