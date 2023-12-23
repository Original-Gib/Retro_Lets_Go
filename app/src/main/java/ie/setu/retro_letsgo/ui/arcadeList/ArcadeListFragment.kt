package ie.setu.retro_letsgo.ui.arcadeList

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.SwitchCompat
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import ie.setu.retro_letsgo.R
import ie.setu.retro_letsgo.adapters.ArcadeAdapter
import ie.setu.retro_letsgo.adapters.ArcadeListener
import ie.setu.retro_letsgo.databinding.FragmentArcadeListBinding
import ie.setu.retro_letsgo.main.MainApp
import ie.setu.retro_letsgo.models.ArcadeModel
import ie.setu.retro_letsgo.ui.auth.LoggedInViewModel
import ie.setu.retro_letsgo.utils.SwipeToDeleteCallback
import ie.setu.retro_letsgo.utils.SwipeToEditCallback
import java.util.Locale

class ArcadeListFragment : Fragment(), ArcadeListener {

    private var _fragBinding: FragmentArcadeListBinding? = null
    private val fragBinding get() = _fragBinding!!
    lateinit var app: MainApp
    private lateinit var arcadeListViewModel: ArcadeListViewModel
    private val loggedInViewModel : LoggedInViewModel by activityViewModels()
    private lateinit var searchView: SearchView
    private lateinit var arcades: ArrayList<ArcadeModel>
    private lateinit var adapter: ArcadeAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setHasOptionsMenu(true)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _fragBinding = FragmentArcadeListBinding.inflate(inflater, container, false)
        val root = fragBinding.root
        setupMenu()
        fragBinding.recyclerView.layoutManager = LinearLayoutManager(activity)

        arcadeListViewModel = ViewModelProvider(this).get(ArcadeListViewModel::class.java)
        arcadeListViewModel.observableArcadesList.observe(viewLifecycleOwner, Observer { arcades ->
            arcades?.let {
                this.arcades = ArrayList(it) // Update the arcades list
                render(arcades)
            }
        })

        searchView = fragBinding.searchView
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                filterList(newText)
                return true
            }

        })


        val fab: FloatingActionButton = fragBinding.fab
        fab.setOnClickListener {
            val action = ArcadeListFragmentDirections.actionArcadeListFragmentToArcadeFragment()
            findNavController().navigate(action)
        }

        val swipeDeleteHandler = object : SwipeToDeleteCallback(requireContext()) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val adapter = fragBinding.recyclerView.adapter as ArcadeAdapter
                adapter.removeAt(viewHolder.adapterPosition)
                arcadeListViewModel.delete(arcadeListViewModel.liveFirebaseUser.value?.uid!!,
                    (viewHolder.itemView.tag as ArcadeModel).uid)
            }
        }
        val itemTouchDeleteHelper = ItemTouchHelper(swipeDeleteHandler)
        itemTouchDeleteHelper.attachToRecyclerView(fragBinding.recyclerView)

        val swipeEditHandler = object : SwipeToEditCallback(requireContext()) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                onArcadeClick(viewHolder.itemView.tag as ArcadeModel)
            }
        }
        val itemTouchEditHelper = ItemTouchHelper(swipeEditHandler)
        itemTouchEditHelper.attachToRecyclerView(fragBinding.recyclerView)

        return root
    }

    private fun setupMenu() {
        (requireActivity() as MenuHost).addMenuProvider(object : MenuProvider {
            override fun onPrepareMenu(menu: Menu) {

            }

            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.menu_arcade_list, menu)

                val item = menu.findItem(R.id.toggleArcades) as MenuItem
                item.setActionView(R.layout.togglebutton_layout)
                val toggleArcades: SwitchCompat = item.actionView!!.findViewById(R.id.toggleButton)
                toggleArcades.isChecked = false

                toggleArcades.setOnCheckedChangeListener { _, isChecked ->
                    if (isChecked) arcadeListViewModel.loadAll()
                    else arcadeListViewModel.load()
                }
            }
            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                // Validate and handle the selected menu item
                return NavigationUI.onNavDestinationSelected(menuItem,
                    requireView().findNavController())
            }     }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

    private fun render(arcadesList: List<ArcadeModel>) {
        fragBinding.recyclerView.adapter = ArcadeAdapter(arcadesList, this, arcadeListViewModel.readOnly.value!!)
        if (arcadesList.isEmpty()) {
            fragBinding.recyclerView.visibility = View.GONE
            fragBinding.arcadesNotFound.visibility = View.VISIBLE
        } else {
            fragBinding.recyclerView.visibility = View.VISIBLE
            fragBinding.arcadesNotFound.visibility = View.GONE
        }
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
        loggedInViewModel.liveFirebaseUser.observe(viewLifecycleOwner, Observer { firebaseUser ->
            if (firebaseUser != null) {
                arcadeListViewModel.liveFirebaseUser.value = firebaseUser
                arcadeListViewModel.load()
            }
        })
    }

    override fun onArcadeClick(arcade: ArcadeModel) {
        val action = ArcadeListFragmentDirections.actionArcadeListFragmentToArcadeDetailsFragment(arcade.uid)
        if(!arcadeListViewModel.readOnly.value!!)
            findNavController().navigate(action)
    }

    private fun filterList(query: String?) {
        query?.let {
            val filteredList = arcades.filter { arcade ->
                arcade.title.toLowerCase(Locale.ROOT).contains(it.toLowerCase(Locale.ROOT))
            }

            if (filteredList.isEmpty()) {
                Snackbar.make(fragBinding.root, "No Arcades Found", Snackbar.LENGTH_SHORT).show()
            } else {
                (fragBinding.recyclerView.adapter as? ArcadeAdapter)?.setFilteredList(filteredList)
            }
        }
    }

}