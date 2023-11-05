package ie.setu.retro_letsgo.activities

import android.content.ActivityNotFoundException
import android.content.Intent
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import ie.setu.retro_letsgo.R
import ie.setu.retro_letsgo.databinding.ActivityGamesBinding
import ie.setu.retro_letsgo.main.MainApp
import ie.setu.retro_letsgo.models.GameModel
import timber.log.Timber.i


class GamesActivity : AppCompatActivity() {

    //declare variables
    var game = GameModel()
    var edit = false
    val REQUEST_IMAGE_CAPTURE = 100
    lateinit var firebaseAuth: FirebaseAuth
    lateinit var app : MainApp
    private lateinit var binding: ActivityGamesBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGamesBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.toolbarAdd.title = "Retro Let's Go - Games"
        setSupportActionBar(binding.toolbarAdd)
        app = application as MainApp

        //init firebase
        firebaseAuth = FirebaseAuth.getInstance()

        //Handle process for when takePicture button is pressed
        binding.takePicture.setOnClickListener {
            val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)

            try{
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
            } catch (e: ActivityNotFoundException) {
                Toast.makeText(this, "Error: " + e.localizedMessage, Toast.LENGTH_SHORT).show()
            }
        }

        i("Game Activity started...")

        //If intent passed has game edit, load the game view populated with game details
        if (intent.hasExtra("game_edit")) {
            edit = true
            game = intent.extras?.getParcelable("game_edit")!!
            binding.btnAddGame.setText(R.string.save_game)
            binding.gameTitle.setText(game.gameTitle)
            binding.gameDescription.setText(game.gameDescription)
            binding.gameSystem.setText(game.gameSystem)
            binding.gameHighScore.setText(game.highScore)
        }

        //handles if addGame button is pressed
        binding.btnAddGame.setOnClickListener() {
            var currentUserId = firebaseAuth.currentUser?.uid
            if (currentUserId != null) {
                game.userId = currentUserId
            }
            game.gameTitle = binding.gameTitle.text.toString()
            game.gameDescription = binding.gameDescription.text.toString()
            game.gameSystem = binding.gameSystem.text.toString()
            game.highScore = binding.gameHighScore.text.toString()
            if (game.gameTitle.isEmpty()) {
                Snackbar.make(it, R.string.enter_game_title, Snackbar.LENGTH_LONG)
                    .show()
            } else {
                if (edit) {
                    app.games.update(game.copy())
                } else {
                    app.games.create(game.copy())
                }
            }
            setResult(RESULT_OK)
            finish()
        }

    }

    //Process for binding captured image to a bitmap
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if(requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK){
            val imageBitmap = data?.extras?.get("data") as Bitmap
            binding.gameImage.setImageBitmap(imageBitmap)
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    //handles display of options for the user in the game view
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_game, menu)
        if (edit) menu.getItem(0).isVisible = true
        return super.onCreateOptionsMenu(menu)
    }

    //handles what to do if an option is selected
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.item_delete -> {
                setResult(99)
                app.games.delete(game)
                finish()
            }        R.id.item_cancel -> { finish() }
        }
        return super.onOptionsItemSelected(item)
    }
}