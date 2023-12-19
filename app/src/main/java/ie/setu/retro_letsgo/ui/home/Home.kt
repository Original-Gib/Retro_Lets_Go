package ie.setu.retro_letsgo.ui.home

import android.annotation.SuppressLint
import android.content.Intent
import android.location.Location
import android.net.Uri
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.firebase.auth.FirebaseUser
import com.squareup.picasso.Picasso
import ie.setu.retro_letsgo.R
import ie.setu.retro_letsgo.databinding.HomeBinding
import ie.setu.retro_letsgo.databinding.NavHeaderBinding
import ie.setu.retro_letsgo.firebase.FirebaseImageManager
import ie.setu.retro_letsgo.utils.customTransformation
import ie.setu.retro_letsgo.ui.auth.LoggedInViewModel
import ie.setu.retro_letsgo.ui.auth.Login
import ie.setu.retro_letsgo.ui.map.MapsViewModel
import ie.setu.retro_letsgo.utils.checkLocationPermissions
import ie.setu.retro_letsgo.utils.isPermissionGranted
import timber.log.Timber


class Home : AppCompatActivity() {

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var homeBinding : HomeBinding
    private lateinit var navHeaderBinding : NavHeaderBinding
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var loggedInViewModel : LoggedInViewModel
    private val mapsViewModel : MapsViewModel by viewModels()
    private lateinit var headerView : View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        homeBinding = HomeBinding.inflate(layoutInflater)
        setContentView(homeBinding.root)
        drawerLayout = homeBinding.drawerLayout
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        val navController = findNavController(R.id.nav_host_fragment)

        appBarConfiguration = AppBarConfiguration(setOf(
            R.id.arcadeFragment, R.id.arcadeListFragment, R.id.gameFragment, R.id.gameListFragment, R.id.mapsFragment), drawerLayout)
        setupActionBarWithNavController(navController, appBarConfiguration)

        val navView = homeBinding.navView
        navView.setupWithNavController(navController)

        val nightModeSwitchItem = navView.menu.findItem(R.id.night_mode)

        nightModeSwitchItem.setOnMenuItemClickListener {
            val mode: Int
             if (nightModeSwitchItem.isChecked) {
                mode = AppCompatDelegate.MODE_NIGHT_YES // Switch to day mode
            } else {
               mode = AppCompatDelegate.MODE_NIGHT_NO // Switch to night mode
            }
            AppCompatDelegate.setDefaultNightMode(mode)
            nightModeSwitchItem.isChecked = !nightModeSwitchItem.isChecked
            true
        }

        if(checkLocationPermissions(this)) {
            mapsViewModel.updateCurrentLocation()
        }

        initNavHeader()
    }

    public override fun onStart() {
        super.onStart()
        loggedInViewModel = ViewModelProvider(this).get(LoggedInViewModel::class.java)
        loggedInViewModel.liveFirebaseUser.observe(this, Observer { firebaseUser ->
            if (firebaseUser != null)
                updateNavHeader(firebaseUser)
        })

        loggedInViewModel.loggedOut.observe(this, Observer { loggedout ->
            if (loggedout) {
                startActivity(Intent(this, Login::class.java))
            }
        })

    }

    private fun updateNavHeader(currentUser: FirebaseUser) {
        FirebaseImageManager.imageUri.observe(this) { result ->
            if (result == Uri.EMPTY) {
                Timber.i("App NO Existing imageUri")
                if (currentUser.photoUrl != null) {
                    //if you're a google user
                    FirebaseImageManager.updateUserImage(
                        currentUser.uid,
                        currentUser.photoUrl,
                        navHeaderBinding.navHeaderImage,
                        false
                    )
                } else {
                    Timber.i("App Loading Existing Default imageUri")
                    FirebaseImageManager.updateDefaultImage(
                        currentUser.uid,
                        R.drawable.pacghost,
                        navHeaderBinding.navHeaderImage
                    )
                }        } else // load existing image from firebase
            {
                Timber.i("App Loading Existing imageUri")
                FirebaseImageManager.updateUserImage(
                    currentUser.uid,
                    FirebaseImageManager.imageUri.value,
                    navHeaderBinding.navHeaderImage, false
                )
            }    }
        navHeaderBinding.navHeaderEmail.text = currentUser.email
        if(currentUser.displayName != null)
            navHeaderBinding.navHeaderName.text = currentUser.displayName
    }
    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    fun signOut(item: MenuItem) {
        loggedInViewModel.logOut()
        //Launch Login activity and clear the back stack to stop navigating back to the Home activity
        val intent = Intent(this, Login::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
    }

    @SuppressLint("MissingPermission")
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (isPermissionGranted(requestCode, grantResults))
            mapsViewModel.updateCurrentLocation()
        else {
            // permissions denied, so use a default location
            mapsViewModel.currentLocation.value = Location("Default").apply {
                latitude = 52.245696
                longitude = -7.139102
            }
        }
        Timber.i("LOC : %s", mapsViewModel.currentLocation.value)
    }

    private fun initNavHeader() {
        Timber.i("App Init Nav Header")
        headerView = homeBinding.navView.getHeaderView(0)
        navHeaderBinding = NavHeaderBinding.bind(headerView)
    }
}