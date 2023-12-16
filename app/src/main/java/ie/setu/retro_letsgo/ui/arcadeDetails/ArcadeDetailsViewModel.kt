package ie.setu.retro_letsgo.ui.arcadeDetails

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ie.setu.retro_letsgo.models.ArcadeManager
import ie.setu.retro_letsgo.models.ArcadeModel

class ArcadeDetailsViewModel : ViewModel() {
    private val arcade = MutableLiveData<ArcadeModel>()

    val observableArcade: LiveData<ArcadeModel>
        get() = arcade

    fun getArcade(id: Long) {
        arcade.value = ArcadeManager.findById(id)
    }
}