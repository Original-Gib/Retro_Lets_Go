package ie.setu.retro_letsgo.models

import android.content.Context
import android.net.Uri
import com.google.gson.*
import com.google.gson.reflect.TypeToken
import ie.setu.retro_letsgo.helpers.*
import timber.log.Timber
import java.lang.reflect.Type
import java.util.*

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

    init {
        if (exists(context, JSON_FILE)) {
            deserialize()
        }
    }

    override fun findAll(): MutableList<ArcadeModel> {
        logAll()
        return arcades
    }

    override fun findByUserId(userId: String): List<ArcadeModel> {
        return arcades.filter { it.userId == userId }
    }

    override fun findById(id: Long): ArcadeModel? {
        return arcades.find { it.id == id }
    }

    override fun create(arcade: ArcadeModel) {
        arcade.id = generateRandomId()
        arcades.add(arcade)
        serialize()
    }


    override fun update(arcade: ArcadeModel) {
        val arcadesList = findAll() as ArrayList<ArcadeModel>
        var foundArcade: ArcadeModel? = arcadesList.find { p -> p.id == arcade.id }
        if (foundArcade != null) {
            foundArcade.title = arcade.title
            foundArcade.description = arcade.description
            foundArcade.phoneNumber = arcade.phoneNumber
            foundArcade.image = arcade.image
            foundArcade.lat = arcade.lat
            foundArcade.lng = arcade.lng
            foundArcade.zoom = arcade.zoom
        }
        serialize()
    }

    override fun delete(arcade: ArcadeModel) {
        arcades.remove(arcade)
        serialize()
    }


    private fun serialize() {
        val jsonString = gsonBuilder.toJson(arcades, listType)
        write(context, JSON_FILE, jsonString)
    }

    private fun deserialize() {
        val jsonString = read(context, JSON_FILE)
        arcades = gsonBuilder.fromJson(jsonString, listType)
    }

    private fun logAll() {
        arcades.forEach { Timber.i("$it") }
    }
}

class UriParser : JsonDeserializer<Uri>,JsonSerializer<Uri> {
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
