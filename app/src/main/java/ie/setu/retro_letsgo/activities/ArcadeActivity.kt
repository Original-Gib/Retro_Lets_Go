package ie.setu.retro_letsgo.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import ie.setu.retro_letsgo.databinding.ActivityRetroLetsGoBinding
import ie.setu.retro_letsgo.models.ArcadeModel
import timber.log.Timber
import timber.log.Timber.i


class ArcadeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRetroLetsGoBinding
    var arcade = ArcadeModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityRetroLetsGoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Timber.plant(Timber.DebugTree())

        i("Retro Lets Go Activity Started")

        binding.btnAdd.setOnClickListener() {
            arcade.title = binding.arcadeTitle.text.toString()
            if (arcade.title.isNotEmpty()) {
                i("Add button pressed: ${arcade.title}")
            } else {
                Snackbar
                    .make(it, "Please enter a title", Snackbar.LENGTH_LONG)
                    .show()
            }
        }
    }
}
