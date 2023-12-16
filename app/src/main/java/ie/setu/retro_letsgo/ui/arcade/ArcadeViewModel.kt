package ie.setu.retro_letsgo.ui.arcade

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ie.setu.retro_letsgo.models.ArcadeManager
import ie.setu.retro_letsgo.models.ArcadeModel

class ArcadeViewModel : ViewModel() {

    private val status = MutableLiveData<Boolean>()

    val observableStatus: LiveData<Boolean>
        get() = status

    fun addArcade(arcade: ArcadeModel) {
        status.value = try {
            ArcadeManager.create(arcade)
            true
        } catch (e: IllegalArgumentException) {
            false
        }
    }
}