package ie.setu.retro_letsgo.main

import android.app.Application
import ie.setu.retro_letsgo.models.ArcadeManager
import ie.setu.retro_letsgo.models.ArcadeStore
import ie.setu.retro_letsgo.models.GameManager
import ie.setu.retro_letsgo.models.GameStore
import timber.log.Timber
import timber.log.Timber.i

class MainApp : Application() {

    lateinit var arcades: ArcadeStore
    lateinit var games: GameStore

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
        arcades = ArcadeManager
        games = GameManager
        i("Retro Lets Go Started")
    }
}