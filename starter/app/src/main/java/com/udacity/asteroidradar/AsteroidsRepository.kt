package com.udacity.asteroidradar
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.udacity.asteroidradar.api.NasaApi
import com.udacity.asteroidradar.api.parseAsteroidsJsonResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import timber.log.Timber
import java.lang.Exception

class AsteroidsRepository(private val database: AsteroidDatabase) {
    val asteroids: LiveData<List<Asteroid>> =
        Transformations.map(database.asteroidDatabaseDao.getAsteroids()) {
            it.asDomainModel()
        }

    val imageOfTheDay: LiveData<ImageOfTheDay> =
        Transformations.map(database.asteroidDatabaseDao.getImageOfTheDay()) {
            it?.asDomainModel()
        }

    suspend fun clearOldAsteroids(){
        try {
            database.asteroidDatabaseDao.clearOld(DateUtils().today())
        } catch (e:Exception) {
            Timber.e("Error while removing old records: ${e.message}")
        }
    }
    suspend fun refreshAsteroids() {
        withContext(Dispatchers.IO) {
            try {
                val apiResponse = NasaApi.retrofitService.getAsteroids()
                if (apiResponse.code() != 200) {
                    Timber.e("Error while accessing NASA API (http): ${apiResponse.code()}.\n Using cached data.")
                } else {  // http status 200 - OK
                    val jsonRoot = JSONObject(apiResponse.body()!!)
                    val asteroids = parseAsteroidsJsonResult(jsonRoot)
                    database.asteroidDatabaseDao.insertAll(*asteroids.asDatabaseModel())
                    Timber.i("Success: ${asteroids.size} asteroids retrieved")
                }
            } catch (e: Exception) {
                Timber.e("Error while getting asteroids from NASA API: ${e.message}")
            }
        }
    }

    suspend fun refreshImageOfTheDay() {
        withContext(Dispatchers.IO) {
            try {
                val apiResponse = NasaApi.retrofitService.getImageOfTheDay()
                if (apiResponse.code() != 200) {
                    Timber.e("Error while accessing NASA API (http): ${apiResponse.code()}.\n Using cached data.")
                } else {  // http status 200 - OK
                    database.asteroidDatabaseDao.insert(apiResponse.body()!!.asDatabaseModel())
                    Timber.i("Success: Image of the day retrieved")
                }
            } catch (e: Exception) {
                Timber.e("Error while getting image from NASA API: ${e.message}")
            }
        }
    }
}