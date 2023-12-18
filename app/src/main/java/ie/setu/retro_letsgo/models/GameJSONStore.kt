package ie.setu.retro_letsgo.models

import android.content.Context
import android.net.Uri
import com.google.gson.*
import com.google.gson.reflect.TypeToken
import ie.setu.retro_letsgo.utils.exists
import ie.setu.retro_letsgo.utils.read
import ie.setu.retro_letsgo.utils.write
import timber.log.Timber
import java.lang.reflect.Type
import java.util.*

const val GAME_JSON_FILE = "games.json"
val gsonGameBuilder: Gson = GsonBuilder().setPrettyPrinting()
    .registerTypeAdapter(Uri::class.java, UriParser())
    .create()
val gameListType: Type = object : TypeToken<ArrayList<GameModel>>() {}.type

class GameJSONStore(private val context: Context) : GameStore {

    var games = mutableListOf<GameModel>()

    init {
        if (exists(context, GAME_JSON_FILE)) {
            deserialize()
        }
    }

    //finds all games
    override fun findAll(): MutableList<GameModel> {
        logAll()
        return games
    }

    override fun findById(id: Long): GameModel? {
        return games.find { it.id == id }
    }


    //creates a new game
    override fun create(game: GameModel) {
        game.id = generateRandomId()
        games.add(game)
        serialize()
    }


    //updates a game with new details
    override fun update(game: GameModel) {
        val gamesList = findAll() as ArrayList<GameModel>
        var foundGame: GameModel? = gamesList.find { p -> p.id == game.id }
        if (foundGame != null) {
            foundGame.gameTitle = game.gameTitle
            foundGame.gameDescription = game.gameDescription
            foundGame.gameSystem = game.gameSystem
            foundGame.highScore = game.highScore
        }
        serialize()
    }

    //deletes a game
    override fun delete(game: GameModel) {
        games.remove(game)
        serialize()
    }

    //finds a game by the user ID
    override fun findByUserId(userId: String): List<GameModel> {
        return games.filter { it.userId == userId }
    }

    private fun serialize() {
        val jsonString = gsonGameBuilder.toJson(games, gameListType)
        write(context, GAME_JSON_FILE, jsonString)
    }

    private fun deserialize() {
        val jsonString = read(context, GAME_JSON_FILE)
        games = gsonGameBuilder.fromJson(jsonString, gameListType)
    }

    private fun logAll() {
        games.forEach { Timber.i("$it") }
    }
}
