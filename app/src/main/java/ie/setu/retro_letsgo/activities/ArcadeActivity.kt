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
import ie.setu.retro_letsgo.models.Location
import timber.log.Timber.i



class ArcadeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRetroLetsGoBinding
    var arcade = ArcadeModel()
    lateinit var app: MainApp
    private lateinit var imageIntentLauncher : ActivityResultLauncher<Intent>
    private lateinit var mapIntentLauncher : ActivityResultLauncher<Intent>
    var edit = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


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
            showImagePicker(imageIntentLauncher,this)
        }

        registerImagePickerCallback()
        registerMapCallback()

        if (intent.hasExtra("arcade_edit")) {
            edit = true
            arcade = intent.extras?.getParcelable("arcade_edit")!!
            binding.arcadeTitle.setText(arcade.title)
            binding.description.setText(arcade.description)
            binding.arcadePhoneNumber.setText(arcade.phoneNumber)
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
            arcade.phoneNumber = binding.arcadePhoneNumber.text.toString()
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

        binding.arcadeLocation.setOnClickListener {
            var location = Location(53.350140, -6.266155, 15f)
            if (arcade.zoom != 0f) {
                location.lat =  arcade.lat
                location.lng = arcade.lng
                location.zoom = arcade.zoom
            }
            val launcherIntent = Intent(this, MapActivity::class.java)
                .putExtra("location", location)
            mapIntentLauncher.launch(launcherIntent)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_arcade, menu)
        if (edit) menu.getItem(0).isVisible = true
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.item_delete -> {
                setResult(99)
                app.arcades.delete(arcade)
                finish()
            }        R.id.item_cancel -> { finish() }
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

                            val image = result.data!!.data!!
                            contentResolver.takePersistableUriPermission(image,
                                Intent.FLAG_GRANT_READ_URI_PERMISSION)
                            arcade.image = image

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

    private fun registerMapCallback() {
        mapIntentLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult())
            { result ->
                when (result.resultCode) {
                    RESULT_OK -> {
                        if (result.data != null) {
                            i("Got Location ${result.data.toString()}")
                            val location = result.data!!.extras?.getParcelable<Location>("location")!!
                            i("Location == $location")
                            arcade.lat = location.lat
                            arcade.lng = location.lng
                            arcade.zoom = location.zoom
                        } // end of if
                    }
                    RESULT_CANCELED -> { } else -> { }
                }
            }
    }
}
