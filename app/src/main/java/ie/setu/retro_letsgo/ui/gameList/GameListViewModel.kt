package ie.setu.retro_letsgo.ui.gameList

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ie.setu.retro_letsgo.models.GameManager
import ie.setu.retro_letsgo.models.GameModel

class GameListViewModel : ViewModel() {

    private val gamesList = MutableLiveData<List<GameModel>>()

    val observableGamesList: LiveData<List<GameModel>>
        get() = gamesList

    init {
        load()
    }

    fun load() {
        gamesList.value = GameManager.findAll()
    }
}