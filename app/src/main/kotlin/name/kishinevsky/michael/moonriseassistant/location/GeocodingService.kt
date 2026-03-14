package name.kishinevsky.michael.moonriseassistant.location

import android.content.Context
import android.location.Geocoder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.IOException

sealed interface GeocodingResult {
    data class Success(val lat: Double, val lng: Double, val displayName: String) : GeocodingResult
    data object NotFound : GeocodingResult
    data class Error(val message: String) : GeocodingResult
}

interface Geocoding {
    suspend fun geocode(query: String): GeocodingResult
}

class GeocodingService(private val context: Context) : Geocoding {

    private val geocoder by lazy { Geocoder(context) }

    @Suppress("deprecation") // getFromLocationName(String, Int) — the callback variant is API 33+
    override suspend fun geocode(query: String): GeocodingResult {
        return withContext(Dispatchers.IO) {
            try {
                val results = geocoder.getFromLocationName(query, 1)
                if (results.isNullOrEmpty()) {
                    GeocodingResult.NotFound
                } else {
                    val address = results[0]
                    val displayName = buildDisplayName(address)
                    GeocodingResult.Success(
                        lat = address.latitude,
                        lng = address.longitude,
                        displayName = displayName,
                    )
                }
            } catch (e: IOException) {
                GeocodingResult.Error(e.message ?: "Geocoding failed")
            }
        }
    }

    private fun buildDisplayName(address: android.location.Address): String {
        val parts = listOfNotNull(address.locality, address.adminArea)
        return if (parts.isNotEmpty()) {
            parts.joinToString(", ")
        } else {
            address.featureName ?: "${address.latitude}, ${address.longitude}"
        }
    }
}
