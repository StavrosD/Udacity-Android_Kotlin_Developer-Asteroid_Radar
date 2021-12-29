package com.udacity.asteroidradar

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface AsteroidDatabaseDao {
    // Asteroid
    @Insert
    suspend fun insert(asteroid: DatabaseAsteroid)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(vararg asteroids:DatabaseAsteroid)

    @Update
    suspend fun update(asteroid: DatabaseAsteroid)

    @Query("SELECT * from asteroid_table WHERE id = :key")
    suspend fun get(key:Long) : DatabaseAsteroid?

    @Query ("DELETE FROM asteroid_table")
    suspend fun clear()

    @Query ("DELETE FROM asteroid_table WHERE close_approach_date < :today") // The API uses the yyyy-MM-dd date format. There is no need to convert to date format, it will give exaclty the same result
    suspend fun clearOld(today : String)

    @Query("SELECT * FROM asteroid_table ORDER BY close_approach_date") // The API uses the yyyy-MM-dd date format. There is no need to convert to date format for sorting because it will give exaclty the same order
    fun getAsteroids() : LiveData<List<DatabaseAsteroid>>

    @Query("SELECT * FROM asteroid_table ORDER BY id DESC LIMIT 1")
    suspend fun getLatestAsteroid() : DatabaseAsteroid?

    // Image of the day
    @Insert
    suspend fun insert(imageOfTheDay: DatabaseImageOfTheDay)

    @Update
    suspend fun update(imageOfTheDay: DatabaseImageOfTheDay)

    @Query ("DELETE FROM image_of_the_day_table")
    suspend fun clearImageOfTheDay()

    @Query("SELECT * FROM image_of_the_day_table ORDER BY id DESC LIMIT 1")
    fun getImageOfTheDay() : LiveData<DatabaseImageOfTheDay>
}