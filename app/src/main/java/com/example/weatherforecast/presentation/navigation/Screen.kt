package com.example.weatherforecast.presentation.navigation

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object CityList : Screen("city_list")
    object CitySearch : Screen("city_search")
    object WeeklyForecast : Screen("weekly_forecast/{cityId}") {
        fun createRoute(cityId: Int) = "weekly_forecast/$cityId"
    }
}
