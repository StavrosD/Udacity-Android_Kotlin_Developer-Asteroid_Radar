package com.udacity.asteroidradar

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "image_of_the_day_table")
data class DatabaseImageOfTheDay (
    @PrimaryKey(autoGenerate = true)
    val id: Long,

    @ColumnInfo(name = "copyright")
    val copyright : String? = null,

    @ColumnInfo(name = "date")
    val date : String,

    @ColumnInfo(name = "explenation")
    val explanation : String,

    @ColumnInfo(name = "hd_url")
    val hdurl : String,

    @ColumnInfo(name = "media_type")
    val mediaType : String,

    @ColumnInfo(name = "service_version")
    val serviceVersion : String,

    @ColumnInfo(name = "title")
    val title : String,

    @ColumnInfo(name = "image_url")
    val imageUrl : String
)


fun DatabaseImageOfTheDay.asDomainModel() : ImageOfTheDay {
    return ImageOfTheDay(id, copyright, date, explanation, hdurl, mediaType, serviceVersion, title, imageUrl )
}

