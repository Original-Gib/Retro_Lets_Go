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
import ie.setu.retro_letsgo.databinding.ActivityArcadeListBinding
import ie.setu.retro_letsgo.main.MainApp
import ie.setu.retro_letsgo.adapters.ArcadeAdapter
import ie.setu.retro_letsgo.adapters.ArcadeListener
import ie.setu.retro_letsgo.models.ArcadeModel


/*
Class is used to display a list of arcades created by the user
 */
class ArcadeListActivity : AppCompatActivity(), ArcadeListener {

    //init variables
    lateinit var app: MainApp
    private var position: Int = 0
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var binding: ActivityArcadeListBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityArcadeListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.toolbar.title = "Arcades"
        setSupportActionBar(binding.toolbar)
        app = application as MainApp

        //init firebase
        firebaseAuth = FirebaseAuth.getInstance()


        val layoutManager = LinearLayoutManager(this)
        binding.recyclerView.layoutManager = layoutManager
        val loggedInUser = firebaseAuth.currentUser?.uid
        if (!loggedInUser.isNullOrEmpty()) {
            binding.recyclerView.adapter =
                ArcadeAdapter(app.arcades.findByUserId(loggedInUser), this) //finds the arcades belonging to a particular user ID
        }
    }

    //Inflate the options for the menu
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    //Handle where to send user if an option is selected
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.item_add -> {
                val launcherIntent = Intent(this, ArcadeActivity::class.java)
                getResults.launch(launcherIntent)
            }
            R.id.item_map -> {
                val launcherIntent = Intent(this, ArcadeMapsActivity::class.java)
                mapIntentLauncher.launch(launcherIntent)
            }
            R.id.item_games -> {
                val launcherIntent = Intent(this, GamesListActivity::class.java)
                mapIntentLauncher.launch(launcherIntent)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    //Handle logic for if an arcade is clicked on
    override fun onArcadeClick(arcade: ArcadeModel, pos: Int) {
        val launcherIntent = Intent(this, ArcadeActivity::class.java)
        launcherIntent.putExtra("arcade_edit", arcade)
        position = pos
        getClickResult.launch(launcherIntent)
    }

    // uses recycler view to get an updated list of arcades for the logged in user
    private val getClickResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        if (it.resultCode == Activity.RESULT_OK) {
            val loggedInUser = firebaseAuth.currentUser?.uid
            if (!loggedInUser.isNullOrEmpty()) {
                val filteredArcades = app.arcades.findByUserId(loggedInUser)
                (binding.recyclerView.adapter as ArcadeAdapter).updateDataSet(filteredArcades)
            }
        } else if (it.resultCode == 99) {
            // Handle arcade deletion here
            (binding.recyclerView.adapter as ArcadeAdapter).removeItem(position)
        }
    }

    private val getResults =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == Activity.RESULT_OK) {
                val loggedInUser = firebaseAuth.currentUser?.uid
                if (!loggedInUser.isNullOrEmpty()) {
                    val filteredArcades = app.arcades.findByUserId(loggedInUser)
                    (binding.recyclerView.adapter as ArcadeAdapter).updateDataSet(filteredArcades)
                }
            }
        }

    private val mapIntentLauncher =
        registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        )    { }

}
