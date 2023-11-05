package ie.setu.retro_letsgo.activities

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import ie.setu.retro_letsgo.R
import ie.setu.retro_letsgo.adapters.ArcadeAdapter
import ie.setu.retro_letsgo.adapters.GameAdapter
import ie.setu.retro_letsgo.adapters.GameListener
import ie.setu.retro_letsgo.databinding.ActivityGamesListBinding
import ie.setu.retro_letsgo.main.MainApp
import ie.setu.retro_letsgo.models.GameModel


class GamesListActivity : AppCompatActivity(), GameListener {

    //declare variables
    private var position: Int = 0
    lateinit var app: MainApp
    lateinit var firebaseAuth: FirebaseAuth
    private lateinit var binding: ActivityGamesListBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGamesListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.toolbar.title = "Games"
        setSupportActionBar(binding.toolbar)
        app = application as MainApp

        //init firebase
        firebaseAuth = FirebaseAuth.getInstance()


        //Gets users games by userID and displays them on the list view
        val layoutManager = LinearLayoutManager(this)
        binding.recyclerView.layoutManager = layoutManager
        val loggedInUser = firebaseAuth.currentUser?.uid
        if (!loggedInUser.isNullOrEmpty()) {
            binding.recyclerView.adapter =
                GameAdapter(app.games.findByUserId(loggedInUser), this)
        }
    }

    //inflates the games list manu options
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_game_list, menu)
        return super.onCreateOptionsMenu(menu)
    }

    //Handles action on selection of a menu option
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.item_add -> {
                val launcherIntent = Intent(this, GamesActivity::class.java)
                getResults.launch(launcherIntent)
            }
            R.id.item_arcade -> {
                val launcherIntent = Intent(this, ArcadeListActivity::class.java)
                getResults.launch(launcherIntent)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onGameClick(game: GameModel, pos: Int) {
        val launcherIntent = Intent(this, GamesActivity::class.java)
        launcherIntent.putExtra("game_edit", game)
        position = pos
        getClickResult.launch(launcherIntent)
    }

    private val getResults =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == Activity.RESULT_OK) {
                val loggedInUser = firebaseAuth.currentUser?.uid
                if (!loggedInUser.isNullOrEmpty()) {
                    val filteredGames = app.games.findByUserId(loggedInUser)
                    (binding.recyclerView.adapter as GameAdapter).updateDataSet(filteredGames)
                }
            }
        }

    private val getClickResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == Activity.RESULT_OK) {
                val loggedInUser = firebaseAuth.currentUser?.uid
                if (!loggedInUser.isNullOrEmpty()) {
                    val filteredGames = app.games.findByUserId(loggedInUser)
                    (binding.recyclerView.adapter as GameAdapter).updateDataSet(filteredGames)
                }
            } else if (it.resultCode == 99) {
                // Handle game deletion here
                (binding.recyclerView.adapter as GameAdapter).removeItem(position)
            }
        }

}