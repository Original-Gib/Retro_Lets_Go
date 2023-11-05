package ie.setu.retro_letsgo.models

interface GameStore {

    fun findAll(): List<GameModel>
    fun create(game: GameModel)

    fun update(game: GameModel)

    fun delete(game: GameModel)

    fun findByUserId(id: String): List<GameModel>
}