package ie.setu.retro_letsgo.ui.arcadeList

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ie.setu.retro_letsgo.models.ArcadeManager
import ie.setu.retro_letsgo.models.ArcadeModel

class ArcadeListViewModel: ViewModel() {

    private val arcadesList = MutableLiveData<List<ArcadeModel>>()

    val observableArcadesList: LiveData<List<ArcadeModel>>
        get() = arcadesList

    init {
        load()
    }

    fun load() {
        arcadesList.value = ArcadeManager.findAll()
    }
}