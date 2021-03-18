package com.mapswithme.workermanagerapp

import android.content.Context
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters

class MySimpleBackgroundTask(context: Context, workerParams: WorkerParameters) :
    Worker(context, workerParams) {







    override fun doWork(): Result {
//        var counter:Int=0
//        var stringToReturn="**PERIODIC_KOTLIN return data is $counter"

        try{
           // counter++

            Log.d("android", "**doWork: background android counter  ")
            return Result.success()
        } catch (e:Exception){
            Log.d("android", "**doWork:EXCEPTION ${e.toString()} ")
            return Result.failure()
        }

    }


}