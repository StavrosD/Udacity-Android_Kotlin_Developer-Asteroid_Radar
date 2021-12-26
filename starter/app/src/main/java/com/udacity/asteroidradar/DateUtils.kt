package com.udacity.asteroidradar

import java.text.SimpleDateFormat
import java.util.*

class DateUtils {
    fun today(): String {
        val calendar = Calendar.getInstance()
        val dateFormat = SimpleDateFormat(Constants.API_QUERY_DATE_FORMAT, Locale.getDefault())
        return dateFormat.format(calendar.time)
    }
    fun dateAfterSixDays():String {
        val calendar = Calendar.getInstance()
        val dateFormat = SimpleDateFormat(Constants.API_QUERY_DATE_FORMAT, Locale.getDefault())
        calendar.add(Calendar.DAY_OF_WEEK, Constants.DEFAULT_END_DATE_DAYS-1)
        return dateFormat.format(calendar.time)
    }
}