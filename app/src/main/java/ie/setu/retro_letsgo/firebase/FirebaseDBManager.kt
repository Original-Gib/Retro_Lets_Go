package ie.setu.retro_letsgo.firebase

import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import ie.setu.retro_letsgo.models.ArcadeModel
import ie.setu.retro_letsgo.models.ArcadeStore
import timber.log.Timber

object FirebaseDBManager : ArcadeStore {
        var database: DatabaseReference = FirebaseDatabase.getInstance("https://retro---let-s-go-default-rtdb.europe-west1.firebasedatabase.app").reference
    override fun findAll(arcadesList: MutableLiveData<List<ArcadeModel>>) {
        TODO("Not yet implemented")
    }

    override fun findAll(userid: String, arcadesList: MutableLiveData<List<ArcadeModel>>) {
        database.child("user-arcades").child(userid)
            .addValueEventListener(object : ValueEventListener {
                override fun onCancelled(error: DatabaseError) {
                    Timber.i("Firebase Donation error : ${error.message}")
                }

                override fun onDataChange(snapshot: DataSnapshot) {
                    val localList = ArrayList<ArcadeModel>()
                    val children = snapshot.children
                    children.forEach {
                        val arcade = it.getValue(ArcadeModel::class.java)
                        localList.add(arcade!!)
                    }
                    database.child("user-arcades").child(userid)
                        .removeEventListener(this)

                    arcadesList.value = localList
                }
            })
    }

        override fun create(firebaseUser: MutableLiveData<FirebaseUser>, arcade: ArcadeModel) {
            Timber.i("Firebase DB Reference : $database")

            val uid = firebaseUser.value!!.uid
            val key = database.child("arcades").push().key
            if (key == null) {
                Timber.i("Firebase Error : Key Empty")
                return
            }
            arcade.uid = key
            val arcadeValues = arcade.toMap()

            val childAdd = HashMap<String, Any>()
            childAdd["/arcades/$key"] = arcadeValues
            childAdd["/user-arcades/$uid/$key"] = arcadeValues

            database.updateChildren(childAdd)
                .addOnSuccessListener {
                    Timber.i("Data added successfully")
                }
                .addOnFailureListener {
                    Timber.e("Error adding data: ${it.message}")
                }

            Timber.i("$arcadeValues")
        }

    override fun delete(userid: String, arcadeid: String) {

        val childDelete : MutableMap<String, Any?> = HashMap()
        childDelete["/arcades/$arcadeid"] = null
        childDelete["/user-arcades/$userid/$arcadeid"] = null

        database.updateChildren(childDelete)
    }

    override fun update(userid: String, arcadeid: String, arcade: ArcadeModel) {

        val arcadeValues = arcade.toMap()

        val childUpdate : MutableMap<String, Any?> = HashMap()
        childUpdate["arcades/$arcadeid"] = arcadeValues
        childUpdate["user-arcades/$userid/$arcadeid"] = arcadeValues

        database.updateChildren(childUpdate)
            .addOnSuccessListener {
            Timber.i("Data updated successfully")
        }
            .addOnFailureListener {
                Timber.e("Error updating data: ${it.message}")
            }

        Timber.i("$arcadeValues")
    }

    override fun findById(userid: String, arcadeid: String, arcade: MutableLiveData<ArcadeModel>) {

        database.child("user-arcades").child(userid)
            .child(arcadeid).get().addOnSuccessListener {
                arcade.value = it.getValue(ArcadeModel::class.java)
                Timber.i("firebase Got value ${it.value}")
            }.addOnFailureListener{
                Timber.e("firebase Error getting data $it")
            }
    }
    }
