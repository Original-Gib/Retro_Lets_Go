package ie.setu.retro_letsgo.activities

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.ajalt.timberkt.i
import com.google.firebase.auth.FirebaseAuth
import ie.setu.retro_letsgo.R
import ie.setu.retro_letsgo.databinding.ActivityArcadeListBinding
import ie.setu.retro_letsgo.main.MainApp
import ie.setu.retro_letsgo.adapters.ArcadeAdapter
import ie.setu.retro_letsgo.adapters.ArcadeListener
import ie.setu.retro_letsgo.models.ArcadeModel


class ArcadeListActivity : AppCompatActivity(), ArcadeListener {
    lateinit var app: MainApp
    private lateinit var binding: ActivityArcadeListBinding
    private var position: Int = 0
    private lateinit var firebaseAuth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityArcadeListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.toolbar.title = title
        setSupportActionBar(binding.toolbar)

        firebaseAuth = FirebaseAuth.getInstance()


        app = application as MainApp

        val layoutManager = LinearLayoutManager(this)
        binding.recyclerView.layoutManager = layoutManager
        val loggedInUser = firebaseAuth.currentUser?.uid
        if (!loggedInUser.isNullOrEmpty()) {
            binding.recyclerView.adapter =
                ArcadeAdapter(app.arcades.findByUserId(loggedInUser), this)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }

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
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onArcadeClick(arcade: ArcadeModel, pos: Int) {
        val launcherIntent = Intent(this, ArcadeActivity::class.java)
        launcherIntent.putExtra("arcade_edit", arcade)
        position = pos
        getClickResult.launch(launcherIntent)
    }

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
