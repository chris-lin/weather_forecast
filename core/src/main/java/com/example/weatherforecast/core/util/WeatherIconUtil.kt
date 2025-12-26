package com.example.weatherforecast.core.util

object WeatherIconUtil {
    private const val ICON_BASE_URL = "https://openweathermap.org/img/wn/"

    fun getIconUrl(iconCode: String, size: IconSize = IconSize.MEDIUM): String {
        val sizeCode = when (size) {
            IconSize.SMALL -> ""
            IconSize.MEDIUM -> "@2x"
            IconSize.LARGE -> "@4x"
        }
        return "$ICON_BASE_URL$iconCode$sizeCode.png"
    }

    enum class IconSize {
        SMALL,
        MEDIUM,
        LARGE
    }
}
