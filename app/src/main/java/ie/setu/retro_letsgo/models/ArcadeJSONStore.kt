package ie.setu.retro_letsgo.models

import android.content.Context
import android.net.Uri
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseUser
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonPrimitive
import com.google.gson.JsonSerializationContext
import com.google.gson.JsonSerializer
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type
import java.util.Random

const val JSON_FILE = "arcades.json"
val gsonBuilder: Gson = GsonBuilder().setPrettyPrinting()
    .registerTypeAdapter(Uri::class.java, UriParser())
    .create()
val listType: Type = object : TypeToken<ArrayList<ArcadeModel>>() {}.type

fun generateRandomId(): Long {
    return Random().nextLong()
}

class ArcadeJSONStore(private val context: Context) : ArcadeStore {

    var arcades = mutableListOf<ArcadeModel>()
    override fun findAll(arcadesList: MutableLiveData<List<ArcadeModel>>) {
        TODO("Not yet implemented")
    }

    override fun findAll(userid: String, arcadesList: MutableLiveData<List<ArcadeModel>>) {
        TODO("Not yet implemented")
    }

    override fun findById(
        userid: String,
        arcadeid: String,
        arcade: MutableLiveData<ArcadeModel>
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

class UriParser : JsonDeserializer<Uri>, JsonSerializer<Uri> {
    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): Uri {
        return Uri.parse(json?.asString)
    }

    override fun serialize(
        src: Uri?,
        typeOfSrc: Type?,
        context: JsonSerializationContext?
    ): JsonElement {
        return JsonPrimitive(src.toString())
    }
}
