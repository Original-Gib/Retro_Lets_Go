package ie.setu.retro_letsgo.ui.arcade

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
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
import androidx.appcompat.widget.Toolbar
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment.Companion.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.squareup.picasso.Picasso
import ie.setu.retro_letsgo.R
import ie.setu.retro_letsgo.activities.MapActivity
import ie.setu.retro_letsgo.databinding.FragmentArcadeBinding
import ie.setu.retro_letsgo.helpers.showImagePicker
import ie.setu.retro_letsgo.main.MainApp
import ie.setu.retro_letsgo.models.ArcadeModel
import ie.setu.retro_letsgo.models.Location
import ie.setu.retro_letsgo.ui.game.gameFragmentDirections


class ArcadeFragment : Fragment() {

    private var _fragBinding: FragmentArcadeBinding? = null
    private val fragBinding get() = _fragBinding!!
    private lateinit var imageIntentLauncher : ActivityResultLauncher<Intent>
    private lateinit var mapIntentLauncher : ActivityResultLauncher<Intent>
    var arcade = ArcadeModel()
    private lateinit var arcadeViewModel: ArcadeViewModel
    lateinit var firebaseAuth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        firebaseAuth = FirebaseAuth.getInstance()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _fragBinding = FragmentArcadeBinding.inflate(inflater, container,false)
        val root = fragBinding.root
        setupMenu()


        val toolbar: Toolbar = root.findViewById(R.id.toolbarAdd)
        (activity as AppCompatActivity).setSupportActionBar(toolbar)
        arcadeViewModel = ViewModelProvider(this).get(ArcadeViewModel::class.java)
        arcadeViewModel.observableStatus.observe(viewLifecycleOwner, {status -> status.let { render(status) }})
        registerImagePickerCallback()
        registerMapCallback()
        setButtonListener(fragBinding)
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _fragBinding = null
    }

    override fun onResume() {
        super.onResume()
        arcadeViewModel = ViewModelProvider(this).get(ArcadeViewModel::class.java)
        arcadeViewModel.observableStatus.observe(viewLifecycleOwner, {status -> status.let { render(status) }})
    }

    fun setButtonListener(layout: FragmentArcadeBinding) {
        layout.chooseImage.setOnClickListener {
            showImagePicker(imageIntentLauncher, this)
        }

        layout.btnAdd.setOnClickListener {
            timber.log.Timber.i("add button pressed")
            var currentUserId = firebaseAuth.currentUser?.uid
            if (currentUserId != null) {
                val userId = currentUserId
            }
            val title = fragBinding.arcadeTitle.text.toString()
            val description = fragBinding.description.text.toString()
            val phoneNumber = fragBinding.arcadePhoneNumber.text.toString()
            if (layout.arcadeTitle.text.toString().isEmpty()) {
                Snackbar
                    .make(it, R.string.enter_arcade_title, Snackbar.LENGTH_LONG)
                    .show()
            } else {
                    arcadeViewModel.addArcade(ArcadeModel(title = title, description = description, phoneNumber = phoneNumber ))
                Snackbar
                    .make(it, R.string.arcade_added, Snackbar.LENGTH_LONG)
                    .show()
            }
        }

        layout.arcadeLocation.setOnClickListener {
            var location = Location(53.350140, -6.266155, 15f)
            if (arcade.zoom != 0f) {
                location.lat =  arcade.lat
                location.lng = arcade.lng
                location.zoom = arcade.zoom
            }
            val launcherIntent = Intent(requireContext(), MapActivity::class.java)
                .putExtra("location", location)
            mapIntentLauncher.launch(launcherIntent)
        }
    }

    private fun registerImagePickerCallback() {
        imageIntentLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult())
            { result ->
                when(result.resultCode){
                    AppCompatActivity.RESULT_OK -> {
                        if (result.data != null) {
                            timber.log.Timber.i("Got Result ${result.data!!.data}")

                            val image = result.data!!.data!!
                            requireContext().contentResolver.takePersistableUriPermission(image,
                                Intent.FLAG_GRANT_READ_URI_PERMISSION)
                            arcade.image = image

                            Picasso.get()
                                .load(arcade.image)
                                .into(fragBinding.arcadeImage)
                            fragBinding.chooseImage.setText(R.string.change_arcade_image)
                        } // end of if
                    }
                    AppCompatActivity.RESULT_CANCELED -> { } else -> { }
                }
            }
    }

    private fun registerMapCallback() {
        mapIntentLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult())
            { result ->
                when (result.resultCode) {
                    AppCompatActivity.RESULT_OK -> {
                        if (result.data != null) {
                            timber.log.Timber.i("Got Location ${result.data.toString()}")
                            val location = result.data!!.extras?.getParcelable<Location>("location")!!
                            timber.log.Timber.i("Location == $location")
                            arcade.lat = location.lat
                            arcade.lng = location.lng
                            arcade.zoom = location.zoom
                        }
                    }
                    AppCompatActivity.RESULT_CANCELED -> { } else -> { }
                }
            }
    }

    private fun setupMenu() {
        (requireActivity() as MenuHost).addMenuProvider(object : MenuProvider {
            override fun onPrepareMenu(menu: Menu) {

            }

            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.menu_arcade, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                // Validate and handle the selected menu item
                return NavigationUI.onNavDestinationSelected(menuItem,
                    requireView().findNavController())
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
            false -> Toast.makeText(context,getString(R.string.arcadeError), Toast.LENGTH_LONG).show()
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

