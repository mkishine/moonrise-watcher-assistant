package name.kishinevsky.michael.moonriseassistant.network.model

import kotlinx.serialization.Serializable

@Serializable
data class TimelineResponse(
    val queryCost: Int,
    val latitude: Double,
    val longitude: Double,
    val resolvedAddress: String,
    val address: String,
    val timezone: String,
    val tzoffset: Double,
    val days: List<DayResponse>,
)

@Serializable
data class DayResponse(
    val datetime: String,
    val tempmax: Double,
    val tempmin: Double,
    val temp: Double,
    val feelslike: Double,
    val windspeed: Double,
    val winddir: Double,
    val cloudcover: Double,
    val precip: Double,
    val precipprob: Double,
    val preciptype: List<String>? = null,
    val conditions: String,
    val moonphase: Double,
    val sunrise: String,
    val sunset: String,
    val hours: List<HourResponse> = emptyList(),
)

@Serializable
data class HourResponse(
    val datetime: String,
    val temp: Double,
    val feelslike: Double,
    val cloudcover: Double,
    val windspeed: Double,
    val winddir: Double,
    val precip: Double,
    val precipprob: Double,
    val preciptype: List<String>? = null,
    val conditions: String,
)
