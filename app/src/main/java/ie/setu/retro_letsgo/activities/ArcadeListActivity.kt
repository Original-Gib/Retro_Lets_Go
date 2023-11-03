package ie.setu.retro_letsgo.activities

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.LinearLayoutManager
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
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityArcadeListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.toolbar.title = title
        setSupportActionBar(binding.toolbar)


        app = application as MainApp

        val layoutManager = LinearLayoutManager(this)
        binding.recyclerView.layoutManager = layoutManager
        binding.recyclerView.adapter = ArcadeAdapter(app.arcades.findAll(), this)
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
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onArcadeClick(arcade: ArcadeModel, pos: Int) {
        val launcherIntent = Intent(this, ArcadeActivity::class.java)
        launcherIntent.putExtra("arcade_edit", arcade)
        position = pos
        getClickResult.launch(launcherIntent)
    }

    private val getClickResult =
        registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) {
            if (it.resultCode == Activity.RESULT_OK) {
                (binding.recyclerView.adapter)?.
                notifyItemRangeChanged(0,app.arcades.findAll().size)
            }
            else
                if (it.resultCode == 99)     (binding.recyclerView.adapter)?.notifyItemRemoved(position)
        }


    private val getResults =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == Activity.RESULT_OK) {
                (binding.recyclerView.adapter)?.notifyItemRangeChanged(0, app.arcades.findAll().size)
            }
        }
}
