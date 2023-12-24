package ie.setu.retro_letsgo.ui.gameDetails

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import ie.setu.retro_letsgo.databinding.FragmentGameDetailsBinding

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
    ): View {

        _fragBinding = FragmentGameDetailsBinding.inflate(inflater, container, false)
        val root = fragBinding.root

        viewModel = ViewModelProvider(this).get(GameDetailsViewModel::class.java)
        viewModel.observableGame.observe(viewLifecycleOwner, Observer { render() })
        return root
    }

    private fun render() {
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