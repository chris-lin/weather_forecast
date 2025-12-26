package com.example.weatherforecast.presentation.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import coil.compose.AsyncImage

@Composable
fun WeatherIcon(
    iconCode: String,
    contentDescription: String,
    modifier: Modifier = Modifier
) {
    val iconUrl = "https://openweathermap.org/img/wn/${iconCode}@2x.png"

    AsyncImage(
        model = iconUrl,
        contentDescription = contentDescription,
        modifier = modifier,
        contentScale = ContentScale.Fit
    )
}
