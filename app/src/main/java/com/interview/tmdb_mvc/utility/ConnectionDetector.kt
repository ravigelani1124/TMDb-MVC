package com.interview.tmdb_mvc.utility

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo

object ConnectionDetector {
    fun isConnectedToInternet(context: Context): Boolean {
        val connectivity = context
                .getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (connectivity != null) {
            val info = connectivity.allNetworkInfo
            if (info != null) for (i in info.indices) if (info[i].state == NetworkInfo.State.CONNECTED) {
                return true
            }
        }
        return false
    }
}