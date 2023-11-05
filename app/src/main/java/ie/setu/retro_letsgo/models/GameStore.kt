package ie.setu.retro_letsgo.models

interface GameStore {

    fun findAll(): List<GameModel>
    fun create(game: GameModel)

    fun update(game: GameModel)
}