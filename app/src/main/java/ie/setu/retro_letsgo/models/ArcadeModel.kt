package ie.setu.retro_letsgo.models

import android.net.Uri
import android.os.Parcelable
import com.google.firebase.database.Exclude
import kotlinx.parcelize.Parcelize

@Parcelize
data class ArcadeModel( var uid: String = "",
                        var email: String? = "joeblggs@test.com",
                        var title: String = "",
                        var description: String = "",
                        var phoneNumber: String = "",
//                        var image: Uri = Uri.EMPTY,
                        var lat : Double = 0.0,
                        var lng: Double = 0.0,
                        var zoom: Float = 0f) : Parcelable

{
    @Exclude
    fun toMap(): Map<String, Any?> {
        return mapOf(
            "uid" to uid,
            "email" to email,
            "title" to title,
            "description" to description,
            "phoneNumber" to phoneNumber,
//            "image" to image.toString(),
            "lat" to lat,
            "lng" to lng,
            "zoom" to zoom
        )
    }
}


@Parcelize
data class Location(var lat: Double = 0.0,
                    var lng: Double = 0.0,
                    var zoom: Float = 0f) : Parcelable