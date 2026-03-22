package name.kishinevsky.michael.moonriseassistant

import android.app.Application
import name.kishinevsky.michael.moonriseassistant.di.AppContainer

class MoonriseApplication : Application() {

    lateinit var container: AppContainer
        private set

    override fun onCreate() {
        super.onCreate()
        container = AppContainer(this)
    }
}
