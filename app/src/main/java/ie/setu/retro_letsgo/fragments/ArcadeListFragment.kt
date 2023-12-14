package ie.setu.retro_letsgo.fragments

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
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import androidx.recyclerview.widget.LinearLayoutManager
import ie.setu.retro_letsgo.R
import ie.setu.retro_letsgo.adapters.ArcadeAdapter
import ie.setu.retro_letsgo.databinding.FragmentArcadeListBinding
import ie.setu.retro_letsgo.main.MainApp

class ArcadeListFragment : Fragment() {

    private var _fragBinding: FragmentArcadeListBinding? = null
    private val fragBinding get() = _fragBinding!!
    lateinit var app: MainApp
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        app = activity?.application as MainApp
        setHasOptionsMenu(true)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _fragBinding = FragmentArcadeListBinding.inflate(inflater, container, false)
        val root = fragBinding.root
        val toolbar: Toolbar = root.findViewById(R.id.toolbar)
        (activity as AppCompatActivity).setSupportActionBar(toolbar)
        activity?.title = getString(R.string.action_arcadeList)
        fragBinding.recyclerView.layoutManager = LinearLayoutManager(requireActivity())


        val adapter = ArcadeAdapter(app.arcades.findAll())
        fragBinding.recyclerView.adapter = adapter

        return root
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

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_arcade_list, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return NavigationUI.onNavDestinationSelected(item,
            requireView().findNavController()) || super.onOptionsItemSelected(item)
    }
}