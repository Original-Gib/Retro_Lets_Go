package ie.setu.retro_letsgo.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.google.android.material.snackbar.Snackbar
import ie.setu.retro_letsgo.R
import ie.setu.retro_letsgo.databinding.ActivityRetroLetsGoBinding
import ie.setu.retro_letsgo.main.MainApp
import ie.setu.retro_letsgo.models.ArcadeModel
import timber.log.Timber.i


class ArcadeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRetroLetsGoBinding
    var arcade = ArcadeModel()
    lateinit var app: MainApp

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityRetroLetsGoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.toolbarAdd.title = title
        setSupportActionBar(binding.toolbarAdd)

        app = application as MainApp
        i("Retro Lets Go Activity Started")

        binding.btnAdd.setOnClickListener() {
            arcade.title = binding.arcadeTitle.text.toString()
            arcade.description = binding.description.text.toString()
            if (arcade.title.isNotEmpty()) {
                app.arcades.add(arcade.copy())
                i("Add button pressed: ${arcade}")
                for (i in app.arcades.indices)
                { i("Arcade[$i]:${this.app.arcades[i]}") }
                setResult(RESULT_OK)
                finish()
            } else {
                Snackbar
                    .make(it, "Please enter a title", Snackbar.LENGTH_LONG)
                    .show()
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_arcade, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.item_cancel -> {
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
