package ie.setu.retro_letsgo.activities

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.google.android.material.snackbar.Snackbar
import com.squareup.picasso.Picasso
import ie.setu.retro_letsgo.R
import ie.setu.retro_letsgo.databinding.ActivityRetroLetsGoBinding
import ie.setu.retro_letsgo.helpers.showImagePicker
import ie.setu.retro_letsgo.main.MainApp
import ie.setu.retro_letsgo.models.ArcadeModel
import timber.log.Timber.i



class ArcadeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRetroLetsGoBinding
    var arcade = ArcadeModel()
    lateinit var app: MainApp
    private lateinit var imageIntentLauncher : ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        var edit = false

        binding = ActivityRetroLetsGoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.toolbarAdd.title = title
        setSupportActionBar(binding.toolbarAdd)

        app = application as MainApp
        i("Retro Lets Go Activity Started")

        binding.chooseImage.setOnClickListener {
            i("Select image")
        }

        binding.chooseImage.setOnClickListener {
            showImagePicker(imageIntentLauncher)
        }

        registerImagePickerCallback()

        if (intent.hasExtra("arcade_edit")) {
            edit = true
            arcade = intent.extras?.getParcelable("arcade_edit")!!
            binding.arcadeTitle.setText(arcade.title)
            binding.description.setText(arcade.description)
            binding.btnAdd.setText(R.string.save_arcade)
            Picasso.get()
                .load(arcade.image)
                .into(binding.arcadeImage)
            if (arcade.image != Uri.EMPTY) {
                binding.chooseImage.setText(R.string.change_arcade_image)
            }
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

    private fun registerImagePickerCallback() {
        imageIntentLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult())
            { result ->
                when(result.resultCode){
                    RESULT_OK -> {
                        if (result.data != null) {
                            i("Got Result ${result.data!!.data}")
                            arcade.image = result.data!!.data!!
                            Picasso.get()
                                .load(arcade.image)
                                .into(binding.arcadeImage)
                            binding.chooseImage.setText(R.string.change_arcade_image)
                        } // end of if
                    }
                    RESULT_CANCELED -> { } else -> { }
                }
            }
    }
}
