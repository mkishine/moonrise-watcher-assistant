package name.kishinevsky.michael.moonriseassistant.navigation

object Routes {
    const val MAIN = "main"
    const val SETTINGS = "settings"
    const val ADD_LOCATION = "addLocation/{isFirstTime}"

    fun addLocation(isFirstTime: Boolean): String = "addLocation/$isFirstTime"
}
