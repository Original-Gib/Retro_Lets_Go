package ie.setu.retro_letsgo.models

import timber.log.Timber.i

class GamesMemStore: GameStore {

    val games = ArrayList<GameModel>()
    override fun findAll(): List<GameModel> {
        return games
        logAll()
    }

    override fun create(game: GameModel) {
        games.add(game)
    }

    override fun update(game: GameModel) {
        var foundGame: GameModel? = games.find { p -> p.id == game.id }
        if (foundGame != null) {
            foundGame.gameTitle = game.gameTitle
            foundGame.gameDescription = game.gameDescription
            logAll()
        }
    }

    override fun delete(game: GameModel) {
        games.remove(game)
    }

    override fun findByUserId(userId: String): List<GameModel> {
        return games.filter { it.userId == userId }
    }

    fun logAll() {
        games.forEach{ i("${it}") }
    }

}