package com.udacity.asteroidradar

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface AsteroidDatabaseDao {
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

    @Query("SELECT * FROM asteroid_table")
    fun getAsteroids() : LiveData<List<DatabaseAsteroid>>

    @Query("SELECT * FROM asteroid_table ORDER BY id DESC LIMIT 1")
    suspend fun getLatestAsteroid() : DatabaseAsteroid?
}