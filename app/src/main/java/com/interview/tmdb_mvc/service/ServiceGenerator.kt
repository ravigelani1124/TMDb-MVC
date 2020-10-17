package com.interview.tmdb_mvc.service

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.widget.Toast
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.google.gson.JsonObject
import com.interview.tmdb_mvc.R
import com.interview.tmdb_mvc.utility.ConnectionDetector
import com.interview.tmdb_mvc.utility.Loger
import com.interview.tmdb_mvc.utility.Util
import com.interview.tmdb_mvc.utility.TinyDb
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class ServiceGenerator {

    private var obj: ServiceGeneratorInterfaceWithFailure? = null
    private var context: Activity
    private lateinit var dialog: Dialog

    interface ServiceGeneratorInterfaceWithFailure {
        fun onSuccess(response: Response<JsonObject?>)
        fun onFailure(call1: Throwable)
    }

    constructor(
        mContext: Activity,
        isLoderShow: Boolean,
        obj1: ServiceGeneratorInterfaceWithFailure,
        call: Call<JsonObject>
    ) {
        obj = obj1
        this.context = mContext

        if (!ConnectionDetector.isConnectedToInternet(context)) {
            Toast.makeText(mContext,mContext.resources.getString(R.string.no_internet),Toast.LENGTH_SHORT).show()
            return
        }

        if (isLoderShow) {
            dialog = Util.showLoading(context)!!
        }

        call.enqueue(object : Callback<JsonObject?> {
            override fun onResponse(call1: Call<JsonObject?>, response: Response<JsonObject?>) {

                if (isLoderShow) {
                    dialog.dismiss()
                }
                try {
                    Loger.LogError("onResponse", " -- " + response.body())
                    if (response.code() == 200) {
                        if (response.body() != null) {
                            obj!!.onSuccess(response)
                        }
                    } else if (response.code() == 401) {
                        val jsonObject = JSONObject(response.errorBody()!!.string())
                        Toast.makeText(mContext,jsonObject.getString("message"),Toast.LENGTH_SHORT).show()
                    } else if (response.code() == 404) {
                        val jsonObject = JSONObject(response.errorBody()!!.string())
                        Toast.makeText(mContext,jsonObject.getString("message"),Toast.LENGTH_SHORT).show()
                    }

                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }

            override fun onFailure(call1: Call<JsonObject?>, t: Throwable) {
                if (isLoderShow) {
                    dialog.dismiss()
                }
                obj!!.onFailure(t)

            }
        })
    }


    companion object {

        fun createAPI(context: Context): ApiInterface {

            val okClient = OkHttpClient.Builder()
                .addInterceptor(ChuckerInterceptor(context))
                .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                .addInterceptor { chain ->
                    val originalRequest = chain.request()
                    val requestBuilder = originalRequest.newBuilder()
                        .method(originalRequest.method, originalRequest.body)
                    val request = requestBuilder.build()
                    chain.proceed(request)
                }
                .addNetworkInterceptor { chain ->
                    val original = chain.request()
                    val requestBuilder = original.newBuilder()
                        .addHeader(
                            "Authorization",
                            "Bearer " + TinyDb(context).getString(TinyDb.oauthToken)
                        )
                        .addHeader("Accept", "application/json")
                    chain.proceed(requestBuilder.build())
                }
                .connectTimeout(1, TimeUnit.MINUTES)
                .readTimeout(1, TimeUnit.MINUTES)
                .build()

            val retrofit: Retrofit = Retrofit.Builder()
                .baseUrl(TinyDb.baseUrl)
                .client(okClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
            return retrofit.create(ApiInterface::class.java)
        }
    }
}