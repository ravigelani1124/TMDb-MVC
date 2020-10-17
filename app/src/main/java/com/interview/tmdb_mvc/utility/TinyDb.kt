package com.ridetechnologies.rider.utility

import android.content.Context
import android.content.SharedPreferences
import androidx.preference.PreferenceManager


class TinyDb {

    companion object {

        const val gender: String = "gender"
        const val baseUrl: String = "http://13.233.92.186:13001/api/"
        const val DEFAULT_STRING = "-1"
        const val fcmToken: String = "fcmToken"
        const val oauthToken = "oauthToken"

        //User Details
        const val userId = "userId"
        const val firstName = "firstName"
        const val userName = "userName"
        const val lastName = "lastName"
        const val email = "email"
        const val profileImage = "profileImage"
        const val phoneNumber = "phoneNumber"
        const val language = "language"
        const val isdCode = "isdCode"
        const val userRating = "userRating"
        const val isUserLogin = "isUserLogin"
        const val userLat="lat"
        const val userLng="lng"

        //Firebase User Details
        const val firebaseUserEmail = "firebaseUserEmail"
        const val firebaseUserId = "firebaseUserId"
        const val firebaseUserPassword = "firebaseUserPassword"

        //firebase server details
        const val gpsTrakingType= "gpsTrakingType"
        const val firebaseRootPath= "firebaseRootPath"
        const val firebaseSubRootPath= "firebaseSubRootPath"

    }

    private var sharedpreferences: SharedPreferences? = null
    private val MyPREFERENCES = "RideTechnologyRider"

    constructor(appContext: Context) {
        sharedpreferences = PreferenceManager.getDefaultSharedPreferences(appContext)
        sharedpreferences = appContext.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE)
    }


    fun getString(key: String): String {
        return sharedpreferences!!.getString(key, DEFAULT_STRING)!!
    }

    fun putString(key: String?, value: String?) {
        if (key == null) {
            throw NullPointerException()
        }
        if (value == null) {
            throw NullPointerException()
        }
        sharedpreferences!!.edit().putString(key, value).apply()
    }

    fun putInt(key: String?, value: Int?) {
        if (key == null) {
            throw NullPointerException()
        }
        if (value == null) {
            throw NullPointerException()
        }
        sharedpreferences!!.edit().putInt(key, value).apply()
    }

    fun getInt(key: String): Int {
        return sharedpreferences!!.getInt(key, 0)
    }

    fun putBoolean(key: String?, value: Boolean) {
        if (key == null) {
            throw NullPointerException()
        }
        sharedpreferences!!.edit().putBoolean(key, value).apply()
    }

    fun getBoolean(key: String): Boolean {
        val DEFAULT_BOOLEAN = false
        return sharedpreferences!!.getBoolean(key, DEFAULT_BOOLEAN)
    }

    fun clear() {

        sharedpreferences!!.edit().clear().apply()
    }
}