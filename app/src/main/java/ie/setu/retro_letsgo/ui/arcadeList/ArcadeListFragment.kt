package ie.setu.retro_letsgo.ui.arcadeList

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
import ie.setu.retro_letsgo.databinding.FragmentArcadeListBinding
import ie.setu.retro_letsgo.main.MainApp
import ie.setu.retro_letsgo.models.ArcadeModel

class ArcadeListFragment : Fragment() {

    private var _fragBinding: FragmentArcadeListBinding? = null
    private val fragBinding get() = _fragBinding!!
    lateinit var app: MainApp
    private lateinit var arcadeListViewModel: ArcadeListViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _fragBinding = FragmentArcadeListBinding.inflate(inflater, container, false)
        val root = fragBinding.root
        setupMenu()
        fragBinding.recyclerView.layoutManager = LinearLayoutManager(activity)
        val toolbar: Toolbar = root.findViewById(R.id.toolbar)
        (activity as AppCompatActivity).setSupportActionBar(toolbar)

        arcadeListViewModel = ViewModelProvider(this).get(ArcadeListViewModel::class.java)
        arcadeListViewModel.observableArcadesList.observe(viewLifecycleOwner, Observer {
                arcades ->
            arcades?.let { render(arcades) }
        })

        val fab: FloatingActionButton = fragBinding.fab
        fab.setOnClickListener {
            val action = ArcadeListFragmentDirections.actionArcadeListFragmentToArcadeFragment()
            findNavController().navigate(action)
        }
        return root
    }

    private fun setupMenu() {
        (requireActivity() as MenuHost).addMenuProvider(object : MenuProvider {
            override fun onPrepareMenu(menu: Menu) {

            }

            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.menu_arcade_list, menu)
            }
            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                // Validate and handle the selected menu item
                return NavigationUI.onNavDestinationSelected(menuItem,
                    requireView().findNavController())
            }     }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

    private fun render(arcadesList: List<ArcadeModel>) {
        fragBinding.recyclerView.adapter = ArcadeAdapter(arcadesList)
    }


    companion object {
        @JvmStatic
        fun newInstance() =
            ArcadeListFragment().apply {
                arguments = Bundle().apply { }
            }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _fragBinding = null
    }

    override fun onResume() {
        super.onResume()
        arcadeListViewModel = ViewModelProvider(this).get(ArcadeListViewModel::class.java)
        arcadeListViewModel.observableArcadesList.observe(viewLifecycleOwner, Observer {
                arcades ->
            arcades?.let { render(arcades) }
        })
    }

}