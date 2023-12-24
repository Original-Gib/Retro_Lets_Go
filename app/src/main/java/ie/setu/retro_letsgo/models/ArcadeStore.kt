package ie.setu.retro_letsgo.models

import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseUser


interface ArcadeStore {
    fun findAll(
        arcadesList:
        MutableLiveData<List<ArcadeModel>>
    )

    fun findAll(
        userid: String,
        arcadesList:
        MutableLiveData<List<ArcadeModel>>
    )

    fun findById(
        userid: String, arcadeid: String,
        arcade: MutableLiveData<ArcadeModel>
    )

    fun create(firebaseUser: MutableLiveData<FirebaseUser>, arcade: ArcadeModel)
    fun delete(userid: String, arcadeid: String)
    fun update(userid: String, arcadeid: String, arcade: ArcadeModel)
}