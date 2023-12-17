package ie.setu.retro_letsgo.ui.gameDetails

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
import ie.setu.retro_letsgo.R
import ie.setu.retro_letsgo.databinding.FragmentArcadeDetailsBinding
import ie.setu.retro_letsgo.databinding.FragmentGameDetailsBinding
import ie.setu.retro_letsgo.ui.arcadeDetails.ArcadeDetailsFragmentArgs
import ie.setu.retro_letsgo.ui.arcadeDetails.ArcadeDetailsViewModel

class GameDetailsFragment : Fragment() {

    private val args by navArgs<GameDetailsFragmentArgs>()
    private var _fragBinding: FragmentGameDetailsBinding? = null
    private val fragBinding get() = _fragBinding!!
    private lateinit var viewModel: GameDetailsViewModel

    companion object {
        fun newInstance() = GameDetailsFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _fragBinding = FragmentGameDetailsBinding.inflate(inflater, container, false)
        val root = fragBinding.root

        viewModel = ViewModelProvider(this).get(GameDetailsViewModel::class.java)
        viewModel.observableGame.observe(viewLifecycleOwner, Observer { render() })
        return root
    }

    private fun render() {
//        fragBinding.description.setText(arcade.description)
//        fragBinding.arcadePhoneNumber.setText(arcade.phoneNumber)
        fragBinding.gamevm = viewModel
    }

    override fun onResume() {
        super.onResume()
        viewModel.getGame(args.id)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _fragBinding = null
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(GameDetailsViewModel::class.java)
        // TODO: Use the ViewModel
    }

}