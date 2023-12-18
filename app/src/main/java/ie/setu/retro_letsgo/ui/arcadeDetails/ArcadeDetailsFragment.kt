package ie.setu.retro_letsgo.ui.arcadeDetails

import android.content.Intent
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.snackbar.Snackbar
import ie.setu.retro_letsgo.R
import ie.setu.retro_letsgo.activities.MapActivity
import ie.setu.retro_letsgo.databinding.FragmentArcadeBinding
import ie.setu.retro_letsgo.databinding.FragmentArcadeDetailsBinding
import ie.setu.retro_letsgo.models.ArcadeModel
import ie.setu.retro_letsgo.models.Location
import ie.setu.retro_letsgo.ui.auth.LoggedInViewModel
import ie.setu.retro_letsgo.utils.showImagePicker

class ArcadeDetailsFragment : Fragment() {
    private val args by navArgs<ArcadeDetailsFragmentArgs>()
    private var _fragBinding: FragmentArcadeDetailsBinding? = null
    private val fragBinding get() = _fragBinding!!
    var arcade = ArcadeModel()
    private lateinit var viewModel: ArcadeDetailsViewModel
    private lateinit var imageIntentLauncher : ActivityResultLauncher<Intent>
    private lateinit var mapIntentLauncher : ActivityResultLauncher<Intent>
    private val loggedInViewModel : LoggedInViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _fragBinding = FragmentArcadeDetailsBinding.inflate(inflater, container, false)
        val root = fragBinding.root

        viewModel = ViewModelProvider(this).get(ArcadeDetailsViewModel::class.java)
        viewModel.observableArcade.observe(viewLifecycleOwner, Observer { render() })

        setButtonListener(fragBinding)

        registerMapCallback()

        return root
    }

    private fun render() {
        fragBinding.arcadevm = viewModel
    }

    override fun onResume() {
        super.onResume()
        viewModel.getArcade(loggedInViewModel.liveFirebaseUser.value?.uid!!,
            args.id)

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _fragBinding = null
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

    fun setButtonListener(layout: FragmentArcadeDetailsBinding) {
//        layout.chooseImage.setOnClickListener {
//            showImagePicker(imageIntentLauncher, this)
//        }

        layout.btnEdit.setOnClickListener {
            if (layout.arcadeTitle.text.toString().isEmpty()) {
                Snackbar.make(it, R.string.enter_arcade_title, Snackbar.LENGTH_LONG).show()
            } else {
                arcade.title = fragBinding.arcadeTitle.text.toString()
                arcade.description = fragBinding.description.text.toString()
                arcade.phoneNumber = fragBinding.arcadePhoneNumber.text.toString()
                arcade.email = loggedInViewModel.liveFirebaseUser.value?.email!!

                viewModel.updateArcade(loggedInViewModel.liveFirebaseUser.value?.uid!!, args.id, arcade)
                Snackbar.make(it, R.string.arcade_saved, Snackbar.LENGTH_LONG).show()
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
}