package ie.setu.retro_letsgo.ui.map

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SwitchCompat
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import ie.setu.retro_letsgo.R
import ie.setu.retro_letsgo.models.ArcadeModel
import ie.setu.retro_letsgo.ui.arcadeList.ArcadeListViewModel
import ie.setu.retro_letsgo.ui.auth.LoggedInViewModel

class MapsFragment : Fragment() {

    private val mapsViewModel: MapsViewModel by activityViewModels()
    private val arcadeListViewModel: ArcadeListViewModel by activityViewModels()
    private val loggedInViewModel: LoggedInViewModel by activityViewModels()

    @SuppressLint("MissingPermission")
    private val callback = OnMapReadyCallback { googleMap ->
        mapsViewModel.map = googleMap
        mapsViewModel.map.isMyLocationEnabled = true
        mapsViewModel.currentLocation.observe(viewLifecycleOwner) {
            val loc = LatLng(
                mapsViewModel.currentLocation.value!!.latitude,
                mapsViewModel.currentLocation.value!!.longitude
            )

            mapsViewModel.map.moveCamera(CameraUpdateFactory.newLatLngZoom(loc, 14f))
            mapsViewModel.map.uiSettings.isZoomControlsEnabled = true
            mapsViewModel.map.uiSettings.isMyLocationButtonEnabled = true
        }
        arcadeListViewModel.observableArcadesList.observe(
            viewLifecycleOwner,
            Observer { arcades ->
                arcades?.let {
                    render(arcades as ArrayList<ArcadeModel>)
                }
            })
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setupMenu()
        return inflater.inflate(R.layout.fragment_maps, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)
    }

    private fun render(arcadesList: ArrayList<ArcadeModel>) {
        var markerColour: Float
        if (arcadesList.isNotEmpty()) {
            mapsViewModel.map.clear()
            arcadesList.forEach {
                markerColour =
                    if (it.email.equals(this.arcadeListViewModel.liveFirebaseUser.value!!.email))
                        BitmapDescriptorFactory.HUE_AZURE + 5
                    else
                        BitmapDescriptorFactory.HUE_RED

                mapsViewModel.map.addMarker(
                    MarkerOptions().position(LatLng(it.lat, it.lng))
                        .title("${it.title} - Phone: ${it.phoneNumber}")
                        .snippet(it.description)
                        .icon(BitmapDescriptorFactory.defaultMarker(markerColour))
                )
            }
        }
    }

    override fun onResume() {
        super.onResume()
        loggedInViewModel.liveFirebaseUser.observe(viewLifecycleOwner) { firebaseUser ->
            if (firebaseUser != null) {
                arcadeListViewModel.liveFirebaseUser.value = firebaseUser
                arcadeListViewModel.load()
            }
        }
    }

    private fun setupMenu() {
        (requireActivity() as MenuHost).addMenuProvider(object : MenuProvider {
            override fun onPrepareMenu(menu: Menu) {
                // Handle for example visibility of menu items
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
                return NavigationUI.onNavDestinationSelected(
                    menuItem,
                    requireView().findNavController()
                )
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }
}