package ie.setu.retro_letsgo.models

import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseUser
import timber.log.Timber.i

var lastId = 0L

internal fun getId(): Long {
    return lastId++
}

object ArcadeManager: ArcadeStore {

    val arcades = ArrayList<ArcadeModel>()

//    override fun findAll(): List<ArcadeModel> {
//        return arcades
//        logAll()
//    }
//
//    override fun create(arcade: ArcadeModel) {
//        arcade.id = getId()
//        arcades.add(arcade)
//    }
//
//    override fun update(arcade: ArcadeModel) {
//        var foundArcade: ArcadeModel? = arcades.find { p -> p.id == arcade.id }
//        if (foundArcade != null) {
//            foundArcade.title = arcade.title
//            foundArcade.description = arcade.description
//            foundArcade.phoneNumber = arcade.phoneNumber
//            foundArcade.image = arcade.image
//            foundArcade.lat = arcade.lat
//            foundArcade.lng = arcade.lng
//            foundArcade.zoom = arcade.zoom
//            logAll()
//        }
//    }
//
//    override fun findById(id:Long) : ArcadeModel? {
//        val foundArcade: ArcadeModel? = arcades.find { it.id == id }
//        return foundArcade
//    }
//
//    override fun delete(arcade: ArcadeModel) {
//        arcades.remove(arcade)
//    }
//
//    override fun findByUserId(userId: String): List<ArcadeModel> {
//        return arcades.filter { it.userId == userId }
//    }

    fun logAll() {
        arcades.forEach{ i("${it}")}
    }

    override fun findAll(arcadesList: MutableLiveData<List<ArcadeModel>>) {
        TODO("Not yet implemented")
    }

    override fun findAll(userid: String, arcadesList: MutableLiveData<List<ArcadeModel>>) {
        TODO("Not yet implemented")
    }

    override fun findById(
        userid: String,
        arcadeid: String,
        donation: MutableLiveData<ArcadeModel>
    ) {
        TODO("Not yet implemented")
    }

    override fun create(firebaseUser: MutableLiveData<FirebaseUser>, arcade: ArcadeModel) {
        TODO("Not yet implemented")
    }


    override fun delete(userid: String, arcadeid: String) {
        TODO("Not yet implemented")
    }

    override fun update(userid: String, arcadeid: String, arcade: ArcadeModel) {
        TODO("Not yet implemented")
    }
}