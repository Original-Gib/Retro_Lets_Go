package ie.setu.retro_letsgo.ui.gameList

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.floatingactionbutton.FloatingActionButton
import ie.setu.retro_letsgo.R
import ie.setu.retro_letsgo.adapters.GameAdapter
import ie.setu.retro_letsgo.adapters.GameListener
import ie.setu.retro_letsgo.databinding.FragmentGameListBinding
import ie.setu.retro_letsgo.main.MainApp
import ie.setu.retro_letsgo.models.GameModel


class GameListFragment : Fragment(), GameListener {

    private var _fragBinding: FragmentGameListBinding? = null
    private val fragBinding get() = _fragBinding!!
    lateinit var app: MainApp
    private lateinit var gameListViewModel: GameListViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _fragBinding = FragmentGameListBinding.inflate(inflater, container, false)
        val root = fragBinding.root
        setupMenu()
        fragBinding.recyclerView.layoutManager = LinearLayoutManager(activity)

        gameListViewModel = ViewModelProvider(this).get(GameListViewModel::class.java)
        gameListViewModel.observableGamesList.observe(viewLifecycleOwner, Observer { arcades ->
            arcades?.let { render(arcades) }
        })

        val fab: FloatingActionButton = fragBinding.fab
        fab.setOnClickListener {
            val action = GameListFragmentDirections.actionGameListFragmentToGameFragment()
            findNavController().navigate(action)
        }
        return root
    }

    private fun setupMenu() {
        (requireActivity() as MenuHost).addMenuProvider(object : MenuProvider {
            override fun onPrepareMenu(menu: Menu) {
            }

            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.menu_game_list, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return NavigationUI.onNavDestinationSelected(
                    menuItem,
                    requireView().findNavController()
                )
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

    private fun render(gamesList: List<GameModel>) {
        fragBinding.recyclerView.adapter = GameAdapter(gamesList, this)
        if (gamesList.isEmpty()) {
            fragBinding.recyclerView.visibility = View.GONE
            fragBinding.gamesNotFound.visibility = View.VISIBLE
        } else {
            fragBinding.recyclerView.visibility = View.VISIBLE
            fragBinding.gamesNotFound.visibility = View.GONE
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            GameListFragment().apply {
                arguments = Bundle().apply { }
            }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _fragBinding = null
    }

    override fun onResume() {
        super.onResume()
        gameListViewModel = ViewModelProvider(this).get(GameListViewModel::class.java)
        gameListViewModel.observableGamesList.observe(viewLifecycleOwner, Observer { arcades ->
            arcades?.let { render(arcades) }
        })
    }

    override fun onGameClick(game: GameModel) {
        val action = GameListFragmentDirections.actionGameListFragmentToGameDetailsFragment(game.id)
        findNavController().navigate(action)
    }

}