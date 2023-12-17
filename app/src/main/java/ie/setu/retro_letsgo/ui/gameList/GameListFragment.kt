package ie.setu.retro_letsgo.ui.gameList

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.floatingactionbutton.FloatingActionButton
import ie.setu.retro_letsgo.R
import ie.setu.retro_letsgo.adapters.ArcadeAdapter
import ie.setu.retro_letsgo.adapters.GameAdapter
import ie.setu.retro_letsgo.adapters.GameListener
import ie.setu.retro_letsgo.databinding.FragmentArcadeListBinding
import ie.setu.retro_letsgo.databinding.FragmentGameListBinding
import ie.setu.retro_letsgo.main.MainApp
import ie.setu.retro_letsgo.models.ArcadeModel
import ie.setu.retro_letsgo.models.GameModel
import ie.setu.retro_letsgo.ui.arcadeList.ArcadeListFragmentDirections
import ie.setu.retro_letsgo.ui.arcadeList.ArcadeListViewModel


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
    ): View? {
        _fragBinding = FragmentGameListBinding.inflate(inflater, container, false)
        val root = fragBinding.root
        setupMenu()
        fragBinding.recyclerView.layoutManager = LinearLayoutManager(activity)
        val toolbar: Toolbar = root.findViewById(R.id.toolbar)
        (activity as AppCompatActivity).setSupportActionBar(toolbar)

        gameListViewModel = ViewModelProvider(this).get(GameListViewModel::class.java)
        gameListViewModel.observableGamesList.observe(viewLifecycleOwner, Observer {
                arcades ->
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
                return NavigationUI.onNavDestinationSelected(menuItem,
                    requireView().findNavController())
            }     }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

    private fun render(gamesList: List<GameModel>) {
        fragBinding.recyclerView.adapter = GameAdapter(gamesList, this)
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
        gameListViewModel.observableGamesList.observe(viewLifecycleOwner, Observer {
                arcades ->
            arcades?.let { render(arcades) }
        })
    }

    override fun onGameClick(game: GameModel) {
        val action = GameListFragmentDirections.actionGameListFragmentToGameDetailsFragment(game.id)
        findNavController().navigate(action)
    }

}