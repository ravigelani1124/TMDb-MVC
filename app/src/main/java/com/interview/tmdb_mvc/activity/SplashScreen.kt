package com.interview.tmdb_mvc.activity

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.interview.tmdb_mvc.R
import com.interview.tmdb_mvc.model.configuration.ConfigurationResponseModel
import com.interview.tmdb_mvc.service.ServiceGenerator
import com.interview.tmdb_mvc.utility.TinyDb
import retrofit2.Response

class SplashScreen : AppCompatActivity() {

    private lateinit var mContext: SplashScreen
    private lateinit var mTinyDb: TinyDb

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
        mContext=this
        initUI()
    }

    private fun initUI() {
        mTinyDb=TinyDb(mContext)
        callConfigurationAPI()
    }

    private fun callConfigurationAPI() {

        ServiceGenerator(mContext, true, object : ServiceGenerator.ServiceGeneratorInterfaceWithFailure {
            override fun onSuccess(response: Response<JsonObject?>) {

                if(response.body()!=null){

                    val configurationResponseModel: ConfigurationResponseModel=Gson().fromJson(response.body().toString(),ConfigurationResponseModel::class.java)
                    val mImagePath=configurationResponseModel.images.secure_base_url+configurationResponseModel.images.poster_sizes[2]
                    mTinyDb.putString(TinyDb.posterImagePath,mImagePath)
                    mTinyDb.putString(TinyDb.configurationResponseModel,Gson().toJson(configurationResponseModel))
                    startActivity(Intent(mContext,HomeScreen::class.java))
                    finishAffinity()
                }
            }

            override fun onFailure(call1: Throwable) {
                Toast.makeText(mContext,call1.message,Toast.LENGTH_SHORT).show()
            }

        },ServiceGenerator.createAPI(mContext).configuration(resources.getString(R.string.api_key)))
    }
}