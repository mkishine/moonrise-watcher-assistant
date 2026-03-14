package name.kishinevsky.michael.moonriseassistant.network

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import name.kishinevsky.michael.moonriseassistant.network.model.TimelineResponse

class VisualCrossingApi(
    private val client: HttpClient,
    private val apiKey: String,
) {

    suspend fun getTimeline(
        latitude: Double,
        longitude: Double,
        unitGroup: String = "us",
    ): TimelineResponse {
        return client.get(
            "$BASE_URL/$latitude,$longitude/next15days"
        ) {
            parameter("unitGroup", unitGroup)
            parameter("key", apiKey)
            parameter("include", "days,hours")
        }.body()
    }

    companion object {
        private const val BASE_URL =
            "https://weather.visualcrossing.com/VisualCrossingWebServices/rest/services/timeline"
    }
}
