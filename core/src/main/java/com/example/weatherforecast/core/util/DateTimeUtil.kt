package com.example.weatherforecast.core.util

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object DateTimeUtil {
    fun formatTimestamp(timestamp: Long, pattern: String = "MMM dd, yyyy HH:mm"): String {
        val date = Date(timestamp)
        val formatter = SimpleDateFormat(pattern, Locale.getDefault())
        return formatter.format(date)
    }

    fun formatUnixTimestamp(unixTimestamp: Long, pattern: String = "HH:mm"): String {
        val date = Date(unixTimestamp * 1000)
        val formatter = SimpleDateFormat(pattern, Locale.getDefault())
        return formatter.format(date)
    }

    fun formatDate(dateString: String, inputPattern: String = "yyyy-MM-dd", outputPattern: String = "EEE, MMM dd"): String {
        return try {
            val inputFormatter = SimpleDateFormat(inputPattern, Locale.getDefault())
            val outputFormatter = SimpleDateFormat(outputPattern, Locale.getDefault())
            val date = inputFormatter.parse(dateString)
            date?.let { outputFormatter.format(it) } ?: dateString
        } catch (e: Exception) {
            dateString
        }
    }
}
