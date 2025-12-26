package com.example.weatherforecast.core.util

object TemperatureUtil {
    fun celsiusToFahrenheit(celsius: Double): Double {
        return (celsius * 9 / 5) + 32
    }

    fun fahrenheitToCelsius(fahrenheit: Double): Double {
        return (fahrenheit - 32) * 5 / 9
    }

    fun formatTemperature(temperature: Double, useCelsius: Boolean = true): String {
        val temp = if (useCelsius) temperature else celsiusToFahrenheit(temperature)
        val unit = if (useCelsius) "°C" else "°F"
        return "${temp.toInt()}$unit"
    }
}
