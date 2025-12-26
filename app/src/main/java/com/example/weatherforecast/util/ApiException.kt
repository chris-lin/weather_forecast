package com.example.weatherforecast.util

sealed class ApiException(message: String) : Exception(message) {
    object NetworkException : ApiException("No internet connection")
    object TimeoutException : ApiException("Request timeout")
    object RateLimitException : ApiException("API rate limit exceeded")
    data class ServerException(val code: Int) : ApiException("Server error: $code")
    object UnknownException : ApiException("Unknown error occurred")
    data class ParseException(val error: String) : ApiException("Failed to parse response: $error")
}
