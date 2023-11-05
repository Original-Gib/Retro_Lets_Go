package ie.setu.retro_letsgo.main

import android.app.Application
import ie.setu.retro_letsgo.models.ArcadeMemStore
import ie.setu.retro_letsgo.models.ArcadeModel
import ie.setu.retro_letsgo.models.ArcadeStore
import ie.setu.retro_letsgo.models.ArcadeJSONStore
import ie.setu.retro_letsgo.models.GameJSONStore
import ie.setu.retro_letsgo.models.GameModel
import ie.setu.retro_letsgo.models.GameStore
import ie.setu.retro_letsgo.models.GamesMemStore
import timber.log.Timber
import timber.log.Timber.i

class MainApp : Application() {

    lateinit var arcades: ArcadeStore
    lateinit var games: GameStore

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
        arcades = ArcadeJSONStore(applicationContext)
        games = GameJSONStore(applicationContext)
        i("Retro Lets Go Started")
    }
}