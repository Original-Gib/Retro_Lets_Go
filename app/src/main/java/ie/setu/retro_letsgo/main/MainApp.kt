package ie.setu.retro_letsgo.main

import android.app.Application
import ie.setu.retro_letsgo.models.ArcadeMemStore
import ie.setu.retro_letsgo.models.ArcadeModel
import ie.setu.retro_letsgo.models.ArcadeStore
import ie.setu.retro_letsgo.models.ArcadeJSONStore
import ie.setu.retro_letsgo.models.GameModel
import ie.setu.retro_letsgo.models.GamesMemStore
import timber.log.Timber
import timber.log.Timber.i

class MainApp : Application() {

    lateinit var arcades: ArcadeStore
    val games = GamesMemStore()

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
        arcades = ArcadeJSONStore(applicationContext)
        i("Retro Lets Go Started")
    }
}