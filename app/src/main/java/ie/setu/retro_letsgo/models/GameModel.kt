package ie.setu.retro_letsgo.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class GameModel(
    var userId: String = "",
    var id: Long = 0,
    var gameTitle: String = "",
    var gameDescription: String = "",
    var gameSystem: String = "",
    var highScore: String = ""
) : Parcelable