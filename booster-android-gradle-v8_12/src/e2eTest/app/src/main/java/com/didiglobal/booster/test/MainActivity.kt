package com.didiglobal.booster.test

import android.app.Activity
import android.os.Bundle
import android.os.HandlerThread
import android.preference.PreferenceManager
import java.util.concurrent.Executors

class MainActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val prefs1 = getSharedPreferences("test_prefs", MODE_PRIVATE)
        prefs1.edit().putString("key1", "value1").apply()

        @Suppress("DEPRECATION")
        val prefs2 = PreferenceManager.getDefaultSharedPreferences(this)
        prefs2.edit().putInt("key2", 42).apply()

        val prefs3 = getPreferences(MODE_PRIVATE)
        prefs3.edit().putBoolean("key3", true).apply()

        val singleThreadExecutor = Executors.newSingleThreadExecutor()
        singleThreadExecutor.execute {}
        singleThreadExecutor.shutdown()

        val fixedThreadPool = Executors.newFixedThreadPool(2)
        fixedThreadPool.execute {}
        fixedThreadPool.shutdown()

        val scheduledExecutor = Executors.newSingleThreadScheduledExecutor()
        scheduledExecutor.shutdown()

        val handlerThread = HandlerThread("booster-thread")
        handlerThread.start()
        handlerThread.quitSafely()
    }
}
