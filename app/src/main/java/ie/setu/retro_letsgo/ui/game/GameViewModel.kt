package ie.setu.retro_letsgo.ui.game

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ie.setu.retro_letsgo.models.GameManager
import ie.setu.retro_letsgo.models.GameModel

class GameViewModel : ViewModel() {

    private val status = MutableLiveData<Boolean>()

    val observableStatus: LiveData<Boolean>
        get() = status

    fun addGame(game: GameModel) {
        status.value = try {
            GameManager.create(game)
            true
        } catch (e: IllegalArgumentException) {
            false
        }
    }
}