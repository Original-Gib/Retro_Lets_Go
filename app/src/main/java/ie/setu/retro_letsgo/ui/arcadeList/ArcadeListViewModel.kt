package ie.setu.retro_letsgo.ui.arcadeList

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseUser
import ie.setu.retro_letsgo.firebase.FirebaseDBManager
import ie.setu.retro_letsgo.models.ArcadeModel
import timber.log.Timber
import java.lang.Exception

class ArcadeListViewModel: ViewModel() {

    private val arcadesList = MutableLiveData<List<ArcadeModel>>()
    var liveFirebaseUser = MutableLiveData<FirebaseUser>()

    val observableArcadesList: LiveData<List<ArcadeModel>>
        get() = arcadesList

    init {
        load()
    }

    fun load() {
//        arcadesList.value = ArcadeManager.findAll()
        try {
            FirebaseDBManager.findAll(liveFirebaseUser.value?.uid!!,arcadesList)
            Timber.i("Report Load Success : ${arcadesList.value.toString()}")
        }
        catch (e: Exception) {
            Timber.i("Report Load Error : $e.message")
        }
    }

    fun delete(userid: String, id: String) {
        try {
            FirebaseDBManager.delete(userid,id)
            Timber.i("Arcade Delete Success")
        }
        catch (e: Exception) {
            Timber.i("Arcade Delete Error : $e.message")
        }
    }
}