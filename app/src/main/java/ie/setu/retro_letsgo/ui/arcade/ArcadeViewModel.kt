package ie.setu.retro_letsgo.ui.arcade

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseUser
import ie.setu.retro_letsgo.firebase.FirebaseDBManager
import ie.setu.retro_letsgo.firebase.FirebaseImageManager
import ie.setu.retro_letsgo.models.ArcadeManager
import ie.setu.retro_letsgo.models.ArcadeModel

class ArcadeViewModel : ViewModel() {

    private val status = MutableLiveData<Boolean>()

    val observableStatus: LiveData<Boolean>
        get() = status

    fun addArcade(firebaseUser: MutableLiveData<FirebaseUser>, arcade: ArcadeModel) {
        status.value = try {
//            ArcadeManager.create(arcade)
            FirebaseDBManager.create(firebaseUser,arcade)
            true
        } catch (e: IllegalArgumentException) {
            false
        }
    }
}