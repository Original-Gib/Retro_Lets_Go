package ie.setu.retro_letsgo.models

interface ArcadeStore {
    fun findAll(): List<ArcadeModel>
    fun create(arcade: ArcadeModel)

    fun update(arcade: ArcadeModel)

    fun delete(arcade: ArcadeModel)
}