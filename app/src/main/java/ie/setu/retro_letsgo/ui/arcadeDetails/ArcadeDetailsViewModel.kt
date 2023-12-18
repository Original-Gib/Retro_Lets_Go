package ie.setu.retro_letsgo.ui.arcadeDetails

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ie.setu.retro_letsgo.firebase.FirebaseDBManager
import ie.setu.retro_letsgo.models.ArcadeManager
import ie.setu.retro_letsgo.models.ArcadeModel
import timber.log.Timber
import java.lang.Exception

class ArcadeDetailsViewModel : ViewModel() {
    private val arcade = MutableLiveData<ArcadeModel>()

    val observableArcade: LiveData<ArcadeModel>
        get() = arcade

    fun getArcade(userid:String, id: String) {
        try {
            FirebaseDBManager.findById(userid, id, arcade)
            Timber.i("Detail getArcade() Success : ${
                arcade.value.toString()}")
        }
        catch (e: Exception) {
            Timber.i("Detail getArcade() Error : $e.message")
        }
    }

    fun updateArcade(userid:String, id: String, arcade: ArcadeModel) {
        try {
            FirebaseDBManager.update(userid, id, arcade)
            Timber.i("Detail update() Success : $arcade")
        }
        catch (e: Exception) {
            Timber.i("Detail update() Error : $e.message")
        }
    }
}