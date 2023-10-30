package ie.setu.retro_letsgo.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ArcadeModel( var id: Long = 0,
                        var title: String = "",
                        var description: String = "") : Parcelable
