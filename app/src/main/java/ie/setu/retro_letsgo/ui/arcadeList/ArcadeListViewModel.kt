package ie.setu.retro_letsgo.ui.arcadeList

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseUser
import ie.setu.retro_letsgo.firebase.FirebaseDBManager
import ie.setu.retro_letsgo.models.ArcadeModel
import timber.log.Timber

class ArcadeListViewModel : ViewModel() {

    private val arcadesList = MutableLiveData<List<ArcadeModel>>()
    var liveFirebaseUser = MutableLiveData<FirebaseUser>()
    var readOnly = MutableLiveData(false)

    val observableArcadesList: LiveData<List<ArcadeModel>>
        get() = arcadesList

    init {
        load()
    }

    //load a users arcades, read-only disabled
    fun load() {
        readOnly.value = false
        try {
            FirebaseDBManager.findAll(liveFirebaseUser.value?.uid!!, arcadesList)
            Timber.i("Arcade List Load Success : ${arcadesList.value.toString()}")
        } catch (e: Exception) {
            Timber.i("Arcade List Load Error : $e.message")
        }
    }

    fun delete(userid: String, id: String) {
        try {
            FirebaseDBManager.delete(userid, id)
            Timber.i("Arcade Delete Success")
        } catch (e: Exception) {
            Timber.i("Arcade Delete Error : $e.message")
        }
    }

    //load all user arcades, read only set to true
    fun loadAll() {
        try {
            readOnly.value = true
            FirebaseDBManager.findAll(arcadesList)
            Timber.i("Report LoadAll Success : ${arcadesList.value.toString()}")
        } catch (e: Exception) {
            Timber.i("Report LoadAll Error : $e.message")
        }
    }
}