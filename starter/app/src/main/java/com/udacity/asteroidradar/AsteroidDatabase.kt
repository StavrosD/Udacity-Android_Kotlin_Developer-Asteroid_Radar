package com.udacity.asteroidradar

import android.content.Context
import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [DatabaseAsteroid::class,DatabaseImageOfTheDay::class],
    autoMigrations = [
        AutoMigration(from = 1,to = 2),
        AutoMigration(from = 2, to =3)
                     ],
    version = 3
)

abstract class AsteroidDatabase : RoomDatabase() {
    abstract val asteroidDatabaseDao : AsteroidDatabaseDao
}



private lateinit var INSTANCE:AsteroidDatabase

fun getDatabase(context: Context) : AsteroidDatabase{
    synchronized(AsteroidDatabase::class.java) {

        if (!::INSTANCE.isInitialized) {
            INSTANCE = Room.databaseBuilder(
                context.applicationContext,
                AsteroidDatabase::class.java,
                "asteroids"
            ).build()
        }
    }
    return INSTANCE
}