package ie.setu.retro_letsgo.ui.arcadeDetails

import android.content.Intent
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import ie.setu.retro_letsgo.databinding.FragmentArcadeDetailsBinding
import ie.setu.retro_letsgo.ui.auth.LoggedInViewModel

class ArcadeDetailsFragment : Fragment() {
    private val args by navArgs<ArcadeDetailsFragmentArgs>()
    private var _fragBinding: FragmentArcadeDetailsBinding? = null
    private val fragBinding get() = _fragBinding!!

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

        fragBinding.btnEdit.setOnClickListener {
            viewModel.updateArcade(loggedInViewModel.liveFirebaseUser.value?.uid!!,
                args.id, fragBinding.arcadevm?.observableArcade!!.value!!)
            findNavController().navigateUp()
        }

        return root
    }

    private fun render() {
//        fragBinding.description.setText(arcade.description)
//        fragBinding.arcadePhoneNumber.setText(arcade.phoneNumber)
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
}