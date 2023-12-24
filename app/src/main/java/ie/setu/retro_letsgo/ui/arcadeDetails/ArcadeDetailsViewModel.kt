package ie.setu.retro_letsgo.ui.arcadeDetails

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ie.setu.retro_letsgo.firebase.FirebaseDBManager
import ie.setu.retro_letsgo.models.ArcadeModel
import timber.log.Timber

class ArcadeDetailsViewModel : ViewModel() {
    private val arcade = MutableLiveData<ArcadeModel>()

    val observableArcade: LiveData<ArcadeModel>
        get() = arcade

    //function to retrieve the arcade from FirebaseDB
    fun getArcade(userid: String, id: String) {
        try {
            FirebaseDBManager.findById(userid, id, arcade)
            Timber.i("Detail getArcade() Success : $arcade")
        } catch (e: Exception) {
            Timber.i("Detail getArcade() Error : $e.message")
        }
    }

    //function to update the details in firebase
    fun updateArcade(userid: String, id: String, arcade: ArcadeModel) {
        try {
            FirebaseDBManager.update(userid, id, arcade)
            Timber.i("Detail update() Success : $arcade")
        } catch (e: Exception) {
            Timber.i("Detail update() Error : $e.message")
        }
    }
}