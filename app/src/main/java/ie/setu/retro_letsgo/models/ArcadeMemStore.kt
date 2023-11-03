package ie.setu.retro_letsgo.models

import timber.log.Timber.i
import timber.log.Timber.log

var lastId = 0L

internal fun getId(): Long {
    return lastId++
}

class ArcadeMemStore: ArcadeStore {

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

    override fun delete(arcade: ArcadeModel) {
        arcades.remove(arcade)
    }

    fun logAll() {
        arcades.forEach{ i("${it}")}
    }
}