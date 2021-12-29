package com.udacity.asteroidradar.api

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.udacity.asteroidradar.Constants
import com.udacity.asteroidradar.DateUtils
import com.udacity.asteroidradar.ImageOfTheDay
import okhttp3.OkHttpClient
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import java.util.concurrent.TimeUnit


interface ApiService {
    @GET("neo/rest/v1/feed")
    suspend fun getAsteroids(
        @Query("start_date") start_date: String = DateUtils().today(),
        @Query("end_date") end_date: String = DateUtils().dateAfterSixDays(),  // 7 days max. Today plus six days
        @Query("api_key") api_key: String = Constants.API_KEY):
            Response<String>

    @GET("planetary/apod")
    suspend fun getImageOfTheDay(
        @Query("api_key") api_key: String = Constants.API_KEY,
        @Query("thumbs") thumbs: String = "True"):  // Return a thumbnail if the APOD is a video
            Response<ImageOfTheDay>
}

object NasaApi{
    private var okHttpClient = OkHttpClient.Builder()   // The IP is very slow, I had to increase the timeout
        .connectTimeout(1, TimeUnit.MINUTES)
        .readTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(15, TimeUnit.SECONDS)
        .build()

    private val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    private val retrofit = Retrofit.Builder()
        .addConverterFactory(ScalarsConverterFactory.create())
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .baseUrl(Constants.NASA_API_BASE_URL)
        .client(okHttpClient)
        .build()



    val retrofitService:ApiService by lazy {
        retrofit.create(ApiService::class.java)
    }
}