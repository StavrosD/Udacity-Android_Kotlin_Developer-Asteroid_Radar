package com.udacity.asteroidradar

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
class ImageOfTheDay(
    val id: Long = -1,
    val copyright : String? = null,
    val date : String,
    val explanation : String,
    val hdurl : String,
    val media_type : String,
    val service_version : String,
    val title : String,
    val url : String)


fun ImageOfTheDay.asDatabaseModel(): DatabaseImageOfTheDay {
    return DatabaseImageOfTheDay(0, copyright, date, explanation, hdurl, media_type, service_version, title, url)
}