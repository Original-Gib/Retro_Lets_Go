package ie.setu.retro_letsgo.models

import timber.log.Timber.i

var lastId = 0L

internal fun getId(): Long {
    return lastId++
}

object ArcadeManager: ArcadeStore {

    val arcades = ArrayList<ArcadeModel>()

    override fun findAll(): List<ArcadeModel> {
        return arcades
        logAll()
    }

    override fun create(arcade: ArcadeModel) {
        arcade.id = getId()
        arcades.add(arcade)
    }

    override fun update(arcade: ArcadeModel) {
        var foundArcade: ArcadeModel? = arcades.find { p -> p.id == arcade.id }
        if (foundArcade != null) {
            foundArcade.title = arcade.title
            foundArcade.description = arcade.description
            foundArcade.phoneNumber = arcade.phoneNumber
            foundArcade.image = arcade.image
            foundArcade.lat = arcade.lat
            foundArcade.lng = arcade.lng
            foundArcade.zoom = arcade.zoom
            logAll()
        }
    }

    override fun findById(id:Long) : ArcadeModel? {
        val foundArcade: ArcadeModel? = arcades.find { it.id == id }
        return foundArcade
    }

    override fun delete(arcade: ArcadeModel) {
        arcades.remove(arcade)
    }

    override fun findByUserId(userId: String): List<ArcadeModel> {
        return arcades.filter { it.userId == userId }
    }

    fun logAll() {
        arcades.forEach{ i("${it}")}
    }
}