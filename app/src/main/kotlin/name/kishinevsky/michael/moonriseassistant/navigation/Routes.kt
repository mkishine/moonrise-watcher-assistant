package name.kishinevsky.michael.moonriseassistant.navigation

object Routes {
    const val MAIN = "main"
    const val SETTINGS = "settings"
    const val ADD_LOCATION = "addLocation/{isFirstTime}"
    const val EDIT_LOCATION = "editLocation/{locationId}"
    const val ABOUT = "about"
    const val TUTORIAL = "tutorial"

    fun addLocation(isFirstTime: Boolean): String = "addLocation/$isFirstTime"
    fun editLocation(locationId: String): String = "editLocation/$locationId"
}
