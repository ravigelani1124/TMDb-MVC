package com.interview.tmdb_mvc.utility

import android.util.Log

object Loger {
    fun LogError(Tag: Any, Message: Any) {
        try {
            Log.e(Tag.toString(), "--$Message")
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}