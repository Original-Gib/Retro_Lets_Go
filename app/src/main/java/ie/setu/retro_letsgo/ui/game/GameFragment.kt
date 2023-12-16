package ie.setu.retro_letsgo.ui.game

import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import ie.setu.retro_letsgo.databinding.FragmentGameBinding
import ie.setu.retro_letsgo.main.MainApp
import ie.setu.retro_letsgo.models.GameModel
import android.Manifest
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import ie.setu.retro_letsgo.R
import ie.setu.retro_letsgo.ui.arcade.ArcadeViewModel
import ie.setu.retro_letsgo.ui.arcadeList.ArcadeListFragmentDirections
import ie.setu.retro_letsgo.ui.gameList.GameListFragment
import ie.setu.retro_letsgo.ui.gameList.GameListFragmentDirections

class gameFragment : Fragment() {


    private var _fragBinding: FragmentGameBinding? = null
    private val fragBinding get() = _fragBinding!!
    var game = GameModel()
    lateinit var firebaseAuth: FirebaseAuth
    val REQUEST_IMAGE_CAPTURE = 100
    private val CAMERA_PERMISSION_REQUEST_CODE = 101
    private lateinit var gameViewModel: GameViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        firebaseAuth = FirebaseAuth.getInstance()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _fragBinding = FragmentGameBinding.inflate(inflater, container,false)
        val root = fragBinding.root
        setupMenu()
        val toolbar: Toolbar = root.findViewById(R.id.toolbarAdd)
        (activity as AppCompatActivity).setSupportActionBar(toolbar)
        gameViewModel = ViewModelProvider(this).get(GameViewModel::class.java)
        gameViewModel.observableStatus.observe(viewLifecycleOwner, {status -> status.let { render(status) }})
        setButtonListener(fragBinding)
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _fragBinding = null
    }

    override fun onResume() {
        super.onResume()
    }

    fun setButtonListener(layout: FragmentGameBinding) {
        fragBinding.takePicture.setOnClickListener {
            // Check if the CAMERA permission is not granted
            if (ContextCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.CAMERA
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                // Request the CAMERA permission
                ActivityCompat.requestPermissions(
                    requireActivity(),
                    arrayOf(Manifest.permission.CAMERA),
                    CAMERA_PERMISSION_REQUEST_CODE
                )
            } else {
                // Permission is already granted, proceed with taking a picture
                dispatchTakePictureIntent()
            }
        }

        fragBinding.btnAddGame.setOnClickListener() {
            var currentUserId = firebaseAuth.currentUser?.uid
            if (currentUserId != null) {
                game.userId = currentUserId
            }
            game.gameTitle = fragBinding.gameTitle.text.toString()
            game.gameDescription = fragBinding.gameDescription.text.toString()
            game.gameSystem = fragBinding.gameSystem.text.toString()
            game.highScore = fragBinding.gameHighScore.text.toString()
            if (game.gameTitle.isEmpty()) {
                Snackbar.make(it, R.string.enter_game_title, Snackbar.LENGTH_LONG)
                    .show()
            } else {
                    gameViewModel.addGame(game)
                Snackbar
                    .make(it, R.string.game_added, Snackbar.LENGTH_LONG)
                    .show()
                }
            }
        }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == CAMERA_PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                dispatchTakePictureIntent()
            } else {
                Snackbar.make(
                    requireView(),
                    "Camera permission denied. Cannot take pictures.",
                    Snackbar.LENGTH_SHORT
                ).show()
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        }
    }

    private fun dispatchTakePictureIntent() {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        try {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
        } catch (e: ActivityNotFoundException) {
            Snackbar.make(requireView(), "Error: " + e.localizedMessage, Toast.LENGTH_SHORT).show()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if(requestCode == REQUEST_IMAGE_CAPTURE && resultCode == AppCompatActivity.RESULT_OK){
            val imageBitmap = data?.extras?.get("data") as Bitmap
            fragBinding.gameImage.setImageBitmap(imageBitmap)
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            gameFragment().apply {
                arguments = Bundle().apply {}
            }
    }

    private fun setupMenu() {
        (requireActivity() as MenuHost).addMenuProvider(object : MenuProvider {
            override fun onPrepareMenu(menu: Menu) {
                // Handle for example visibility of menu items
            }

            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menu.clear()
                menuInflater.inflate(R.menu.menu_game, menu)
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
                    val action = gameFragmentDirections.actionGameFragmentToGameListFragment()
                    findNavController().navigate(action)
                }
            }
            false -> Toast.makeText(context,getString(R.string.gameError), Toast.LENGTH_LONG).show()
        }
    }
}