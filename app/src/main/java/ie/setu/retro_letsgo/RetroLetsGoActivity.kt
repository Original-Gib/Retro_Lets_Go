package ie.setu.retro_letsgo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import timber.log.Timber
import timber.log.Timber.i

class RetroLetsGoActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_retro_lets_go)

        Timber.plant(Timber.DebugTree())

        i("Retro Lets Go Activity Started")
    }
}