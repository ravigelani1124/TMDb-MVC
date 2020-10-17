package com.interview.tmdb_mvc.utility

import android.os.StrictMode
import android.os.StrictMode.VmPolicy
import androidx.multidex.MultiDex
import androidx.multidex.MultiDexApplication

class ApplicationClass : MultiDexApplication() {

    override fun onCreate() {
        super.onCreate()
        MultiDex.install(this)
        mInstance = this
        val builder = VmPolicy.Builder()
        StrictMode.setVmPolicy(builder.build())
        val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)
    }

    companion object {
        private var mInstance: ApplicationClass? = null
    }
}