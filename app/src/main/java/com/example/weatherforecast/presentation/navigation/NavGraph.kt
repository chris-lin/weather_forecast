package com.example.weatherforecast.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.weatherforecast.presentation.ui.citylist.CityListScreen
import com.example.weatherforecast.presentation.ui.citysearch.CitySearchScreen
import com.example.weatherforecast.presentation.ui.home.HomeScreen
import com.example.weatherforecast.presentation.ui.weeklyforecast.WeeklyForecastScreen

@Composable
fun WeatherNavGraph(
    navController: NavHostController
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Home.route
    ) {
        composable(Screen.Home.route) {
            HomeScreen(
                onNavigateToCityList = {
                    navController.navigate(Screen.CityList.route)
                }
            )
        }

        composable(Screen.CityList.route) {
            CityListScreen(
                onNavigateBack = {
                    navController.popBackStack()
                },
                onNavigateToSearch = {
                    navController.navigate(Screen.CitySearch.route)
                },
                onCityClick = { cityId ->
                    navController.navigate(Screen.WeeklyForecast.createRoute(cityId))
                }
            )
        }

        composable(Screen.CitySearch.route) {
            CitySearchScreen(
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }

        composable(
            route = Screen.WeeklyForecast.route,
            arguments = listOf(
                navArgument("cityId") { type = NavType.StringType }
            )
        ) {
            WeeklyForecastScreen(
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }
    }
}
