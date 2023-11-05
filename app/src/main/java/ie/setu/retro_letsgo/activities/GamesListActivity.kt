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
import ie.setu.retro_letsgo.adapters.GameAdapter
import ie.setu.retro_letsgo.adapters.GameListener
import ie.setu.retro_letsgo.databinding.ActivityGamesListBinding
import ie.setu.retro_letsgo.main.MainApp
import ie.setu.retro_letsgo.models.GameModel


class GamesListActivity : AppCompatActivity(), GameListener {

    lateinit var app: MainApp
    private lateinit var binding: ActivityGamesListBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGamesListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.toolbar.title = "Retro Let's Go - Games"
        setSupportActionBar(binding.toolbar)

        app = application as MainApp

        val layoutManager = LinearLayoutManager(this)
        binding.recyclerView.layoutManager = layoutManager
        binding.recyclerView.adapter = GameAdapter(app.games.findAll(), this)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_game_list, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.item_add -> {
                val launcherIntent = Intent(this, GamesActivity::class.java)
                getResult.launch(launcherIntent)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private val getResult =
        registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) {
            if (it.resultCode == Activity.RESULT_OK) {
                (binding.recyclerView.adapter)?.
                notifyItemRangeChanged(0,app.games.findAll().size)
            }
        }

    override fun onGameClick(game: GameModel) {
        val launcherIntent = Intent(this, GamesActivity::class.java)
        launcherIntent.putExtra("game_edit", game)
        getClickResult.launch(launcherIntent)
    }

    private val getClickResult =
        registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) {
            if (it.resultCode == Activity.RESULT_OK) {
                (binding.recyclerView.adapter)?.
                notifyItemRangeChanged(0,app.games.findAll().size)
            }
        }

}
