package com.example.passvault.utils.helper

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

object DateTimeHelper {
    private fun getDayWithSuffix(day: Int): String {
        return when {
            day in 11..13 -> "${day}th"
            day % 10 == 1 -> "${day}st"
            day % 10 == 2 -> "${day}nd"
            day % 10 == 3 -> "${day}rd"
            else -> "${day}th"
        }
    }
    @RequiresApi(Build.VERSION_CODES.O)
    fun formatSupabaseTimestamp(input: String): String {
        val dateTime = OffsetDateTime.parse(input)

        val dayWithSuffix = getDayWithSuffix(dateTime.dayOfMonth)
        val monthFormatter = DateTimeFormatter.ofPattern("MMM", Locale.ENGLISH) // Jan, Feb, etc.
        val month = dateTime.format(monthFormatter)
        val year = dateTime.year
        val timeFormatter = DateTimeFormatter.ofPattern("h:mm a", Locale.ENGLISH)
        val time = dateTime.format(timeFormatter)

        return "$dayWithSuffix $month $year - $time"
    }
}