package ie.setu.retro_letsgo.ui.arcade

import android.app.Activity.RESULT_CANCELED
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import com.google.android.material.snackbar.Snackbar
import com.squareup.picasso.Picasso
import ie.setu.retro_letsgo.R
import ie.setu.retro_letsgo.activities.MapActivity
import ie.setu.retro_letsgo.databinding.FragmentArcadeBinding
import ie.setu.retro_letsgo.firebase.FirebaseImageManager
import ie.setu.retro_letsgo.models.ArcadeModel
import ie.setu.retro_letsgo.models.Location
import ie.setu.retro_letsgo.ui.auth.LoggedInViewModel
import ie.setu.retro_letsgo.utils.showImagePicker
import timber.log.Timber


class ArcadeFragment : Fragment() {

    //defining the variables
    private var _fragBinding: FragmentArcadeBinding? = null
    private val fragBinding get() = _fragBinding!!
    private lateinit var imageIntentLauncher: ActivityResultLauncher<Intent>
    private lateinit var intentLauncher: ActivityResultLauncher<Intent>
    private lateinit var mapIntentLauncher: ActivityResultLauncher<Intent>
    var arcade = ArcadeModel()
    private lateinit var arcadeViewModel: ArcadeViewModel
    private val loggedInViewModel: LoggedInViewModel by activityViewModels()

    //creating an inflating the view and setting the binding to be associated with the arcadeFragment
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _fragBinding = FragmentArcadeBinding.inflate(inflater, container, false)
        val root = fragBinding.root
        setupMenu()

        arcadeViewModel = ViewModelProvider(this).get(ArcadeViewModel::class.java)
        arcadeViewModel.observableStatus.observe(
            viewLifecycleOwner,
            { status -> status.let { render(status) } })
        registerImagePickerCallback()
        registerMapCallback()
        setButtonListener(fragBinding)
        return root
    }

    //clearing the frag biding when the view is destroyed
    override fun onDestroyView() {
        super.onDestroyView()
        _fragBinding = null
    }

    ///retrieving details from the view model when the fragment is resumed
    override fun onResume() {
        super.onResume()
        arcadeViewModel = ViewModelProvider(this).get(ArcadeViewModel::class.java)
        arcadeViewModel.observableStatus.observe(
            viewLifecycleOwner,
            { status -> status.let { render(status) } })
    }

    //defining logic for the buttons bound in the view
    fun setButtonListener(layout: FragmentArcadeBinding) {
        layout.chooseImage.setOnClickListener {
            showImagePicker(intentLauncher, this)
        }

        layout.btnAdd.setOnClickListener {
            if (layout.arcadeTitle.text.toString().isEmpty()) {
                Snackbar.make(it, R.string.enter_arcade_title, Snackbar.LENGTH_LONG).show()
            } else {
                arcade.title = fragBinding.arcadeTitle.text.toString()
                arcade.description = fragBinding.description.text.toString()
                arcade.phoneNumber = fragBinding.arcadePhoneNumber.text.toString()
                arcade.email = loggedInViewModel.liveFirebaseUser.value?.email!!

                arcadeViewModel.addArcade(loggedInViewModel.liveFirebaseUser, arcade)
                Snackbar.make(it, R.string.arcade_added, Snackbar.LENGTH_LONG).show()
            }
        }

        layout.arcadeLocation.setOnClickListener {
            var location = Location(53.350140, -6.266155, 15f)
            if (arcade.zoom != 0f) {
                location.lat = arcade.lat
                location.lng = arcade.lng
                location.zoom = arcade.zoom
            }
            val launcherIntent = Intent(requireContext(), MapActivity::class.java)
                .putExtra("location", location)
            mapIntentLauncher.launch(launcherIntent)
        }
    }

    //regsitering access to the image picker
    private fun registerImagePickerCallback() {
        intentLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                when (result.resultCode) {
                    RESULT_OK -> {
                        result.data?.data?.let { uri ->
                            Timber.i("Selected image URI: $uri")
                            arcade.image = uri.toString()
                            Picasso.get().load(uri).into(fragBinding.arcadeImage)
                            FirebaseImageManager.uploadArcadeImage(arcade) { imageUrl ->
                                arcade.image = imageUrl
                            }
                        }
                    }

                    RESULT_CANCELED -> Timber.i("Image selection cancelled")
                    else -> Timber.e("Unexpected result code $result.resultCode")
                }
            }
    }

    //call back to access the map to add in the arcade location
    private fun registerMapCallback() {
        mapIntentLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult())
            { result ->
                when (result.resultCode) {
                    AppCompatActivity.RESULT_OK -> {
                        if (result.data != null) {
                            Timber.i("Got Location ${result.data.toString()}")
                            val location =
                                result.data!!.extras?.getParcelable<Location>("location")!!
                            Timber.i("Location == $location")
                            arcade.lat = location.lat
                            arcade.lng = location.lng
                            arcade.zoom = location.zoom
                        }
                    }

                    AppCompatActivity.RESULT_CANCELED -> {}
                    else -> {}
                }
            }
    }

    //setting up the menu_arcade for displaying while in an arcade fragment
    private fun setupMenu() {
        (requireActivity() as MenuHost).addMenuProvider(object : MenuProvider {
            override fun onPrepareMenu(menu: Menu) {

            }

            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.menu_arcade, menu)
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

    private fun render(status: Boolean) {
        when (status) {
            true -> {
                view?.let {
                    //Uncomment this if you want to immediately return to Report
                    findNavController().popBackStack(R.id.arcadeListFragment, false)
                }
            }

            false -> Toast.makeText(context, getString(R.string.arcadeError), Toast.LENGTH_LONG)
                .show()
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            ArcadeFragment().apply {
                arguments = Bundle().apply {}
            }
    }

}

