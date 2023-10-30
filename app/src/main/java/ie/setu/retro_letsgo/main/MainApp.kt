package ie.setu.retro_letsgo.main

import android.app.Application
import ie.setu.retro_letsgo.models.ArcadeModel
import timber.log.Timber
import timber.log.Timber.i

class MainApp : Application() {

    val arcades = ArrayList<ArcadeModel>()

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
        i("Retro Lets Go Started")
    }
}