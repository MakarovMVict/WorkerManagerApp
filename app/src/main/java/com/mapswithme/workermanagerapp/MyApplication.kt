package com.mapswithme.workermanagerapp

import android.app.Application
import android.util.Log
import androidx.work.Configuration
import androidx.work.WorkManager

class MyApplication() : Application(), Configuration.Provider {


    override fun onCreate() {
        super.onCreate()
        Log.i("app", "**onCreate:IN APP!! ")
        //print("**onCreate")

        val myConfig = Configuration.Builder()
            .setMinimumLoggingLevel(android.util.Log.INFO)
            .build()

// initialize WorkManager
        WorkManager.initialize(this, myConfig)


    }






    override fun getWorkManagerConfiguration(): Configuration {
        return Configuration.Builder()
           // .setMinimumLoggingLevel(android.util.Log.INFO)
            .build()
    }


//




}