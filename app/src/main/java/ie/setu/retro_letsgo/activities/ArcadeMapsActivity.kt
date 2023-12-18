package ie.setu.retro_letsgo.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.auth.FirebaseAuth
import com.squareup.picasso.Picasso
import ie.setu.retro_letsgo.databinding.ActivityArcadeMapsBinding
import ie.setu.retro_letsgo.databinding.ContentArcadeMapsBinding
import ie.setu.retro_letsgo.main.MainApp

/*
Class is used to handle the display of arcades on maps for a user
 */
class ArcadeMapsActivity : AppCompatActivity(), GoogleMap.OnMarkerClickListener {

    //init variables
    private lateinit var binding: ActivityArcadeMapsBinding
    private lateinit var contentBinding: ContentArcadeMapsBinding
    lateinit var map: GoogleMap
    lateinit var app: MainApp
    lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //Init firebase
        firebaseAuth = FirebaseAuth.getInstance()

        app = application as MainApp
        binding = ActivityArcadeMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        contentBinding = ContentArcadeMapsBinding.bind(binding.root)
        contentBinding.mapView.onCreate(savedInstanceState)
        contentBinding.mapView.getMapAsync {
            map = it
            configureMap()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        contentBinding.mapView.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        contentBinding.mapView.onLowMemory()
    }

    override fun onPause() {
        super.onPause()
        contentBinding.mapView.onPause()
    }

    override fun onResume() {
        super.onResume()
        contentBinding.mapView.onResume()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        contentBinding.mapView.onSaveInstanceState(outState)
    }

    private fun configureMap() {
        map.uiSettings.isZoomControlsEnabled = true
        map.setOnMarkerClickListener(this)
        val loggedInUser = firebaseAuth.currentUser?.uid
        if (!loggedInUser.isNullOrEmpty()) {
//            app.arcades.findByUserId(loggedInUser).forEach {
//                val loc = LatLng(it.lat, it.lng)
//                val options = MarkerOptions().title(it.title).position(loc)
//                map.addMarker(options)?.tag = it.id
//                map.moveCamera(CameraUpdateFactory.newLatLngZoom(loc, it.zoom))
            }
        }


    override fun onMarkerClick(marker: Marker): Boolean {
//        val tag = marker.tag as Long
//        val arcade = app.arcades.findById(tag)
//        contentBinding.currentTitle.text = arcade!!.title
//        contentBinding.currentDescription.text = arcade.description
//        Picasso.get().load(arcade.image).into(contentBinding.currentImage)
        return false
    }
}