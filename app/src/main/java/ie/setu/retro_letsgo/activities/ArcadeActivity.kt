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

        var edit = false

        binding = ActivityRetroLetsGoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.toolbarAdd.title = title
        setSupportActionBar(binding.toolbarAdd)

        app = application as MainApp
        i("Retro Lets Go Activity Started")

        if (intent.hasExtra("arcade_edit")) {
            edit = true
            arcade = intent.extras?.getParcelable("arcade_edit")!!
            binding.arcadeTitle.setText(arcade.title)
            binding.description.setText(arcade.description)
            binding.btnAdd.setText(R.string.save_arcade)
        }

        binding.btnAdd.setOnClickListener() {
            arcade.title = binding.arcadeTitle.text.toString()
            arcade.description = binding.description.text.toString()
            if (arcade.title.isEmpty()) {
                Snackbar
                    .make(it, R.string.enter_arcade_title, Snackbar.LENGTH_LONG)
                    .show()
            } else {
                if (edit) {
                    app.arcades.update(arcade.copy())
                } else {
                    app.arcades.create(arcade.copy())
                }
            }
            setResult(RESULT_OK)
            finish()
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
