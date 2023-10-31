package ie.setu.retro_letsgo.helpers

import android.content.Intent
import androidx.activity.result.ActivityResultLauncher
import ie.setu.retro_letsgo.R

fun showImagePicker(intentLauncher : ActivityResultLauncher<Intent>) {
    var chooseFile = Intent(Intent.ACTION_OPEN_DOCUMENT)
    chooseFile.type = "image/*"
    chooseFile = Intent.createChooser(chooseFile, R.string.select_arcade_image.toString())
    intentLauncher.launch(chooseFile)
}