package ie.setu.retro_letsgo.ui.gameDetails

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ie.setu.retro_letsgo.models.GameManager
import ie.setu.retro_letsgo.models.GameModel

class GameDetailsViewModel : ViewModel() {
    private val game = MutableLiveData<GameModel>()

    val observableGame: LiveData<GameModel>
        get() = game

    fun getGame(id: Long) {
        game.value = GameManager.findById(id)
    }
}