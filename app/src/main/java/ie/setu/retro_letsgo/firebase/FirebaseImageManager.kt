package ie.setu.retro_letsgo.firebase

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.net.Uri
import android.widget.ImageView
import androidx.lifecycle.MutableLiveData
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.UploadTask
import com.squareup.picasso.MemoryPolicy
import com.squareup.picasso.Picasso
import com.squareup.picasso.Target
import ie.setu.retro_letsgo.models.ArcadeModel
import ie.setu.retro_letsgo.utils.customTransformation
import timber.log.Timber
import java.io.ByteArrayOutputStream
import java.util.UUID


object FirebaseImageManager {

    var storage = FirebaseStorage.getInstance().reference
    var imageUri = MutableLiveData<Uri>()

    //Retrieve profile picture from firebase if it exists. If not display the pacghost image
    fun checkStorageForExistingProfilePic(userid: String) {
        val imageRef = storage.child("photos").child("${userid}.jpg")
        val defaultImageRef = storage.child("pacghost")

        imageRef.metadata.addOnSuccessListener { //File Exists
            imageRef.downloadUrl.addOnCompleteListener { task ->
                imageUri.value = task.result!!
            }
            //File Doesn't Exist
        }.addOnFailureListener {
            imageUri.value = Uri.EMPTY
        }
    }

    //function to upload an image to firebase under the photos folder
    fun uploadImageToFirebase(userid: String, bitmap: Bitmap, updating: Boolean) {
        // Get the data from an ImageView as bytes
        val imageRef = storage.child("photos").child("${userid}.jpg")
        //val bitmap = (imageView as BitmapDrawable).bitmap
        val baos = ByteArrayOutputStream()
        lateinit var uploadTask: UploadTask

        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val data = baos.toByteArray()

        imageRef.metadata.addOnSuccessListener { //File Exists
            if (updating) // Update existing Image
            {
                uploadTask = imageRef.putBytes(data)
                uploadTask.addOnSuccessListener { ut ->
                    ut.metadata!!.reference!!.downloadUrl.addOnCompleteListener { task ->
                        imageUri.value = task.result!!
                    }
                }
            }
        }.addOnFailureListener { //File Doesn't Exist
            uploadTask = imageRef.putBytes(data)
            uploadTask.addOnSuccessListener { ut ->
                ut.metadata!!.reference!!.downloadUrl.addOnCompleteListener { task ->
                    imageUri.value = task.result!!
                }
            }
        }
    }

    //function to update the image of a logged in user
    fun updateUserImage(userid: String, imageUri: Uri?, imageView: ImageView, updating: Boolean) {
        Picasso.get().load(imageUri)
            .resize(200, 200)
            .transform(customTransformation())
            .memoryPolicy(MemoryPolicy.NO_CACHE)
            .centerCrop()
            .into(object : Target {
                override fun onBitmapLoaded(
                    bitmap: Bitmap?,
                    from: Picasso.LoadedFrom?
                ) {
                    Timber.i("Retro - Lets Go onBitmapLoaded $bitmap")
                    uploadImageToFirebase(userid, bitmap!!, updating)
                    imageView.setImageBitmap(bitmap)
                }

                override fun onBitmapFailed(
                    e: java.lang.Exception?,
                    errorDrawable: Drawable?
                ) {
                    Timber.i("Retro - Lets Go onBitmapFailed $e")
                }

                override fun onPrepareLoad(placeHolderDrawable: Drawable?) {}
            })
    }


    fun updateDefaultImage(userid: String, resource: Int, imageView: ImageView) {
        Picasso.get().load(resource)
            .resize(200, 200)
            .transform(customTransformation())
            .memoryPolicy(MemoryPolicy.NO_CACHE)
            .centerCrop()
            .into(object : Target {
                override fun onBitmapLoaded(
                    bitmap: Bitmap?,
                    from: Picasso.LoadedFrom?
                ) {
                    Timber.i("Retro - Lets Go onBitmapLoaded $bitmap")
                    uploadImageToFirebase(userid, bitmap!!, false)
                    imageView.setImageBitmap(bitmap)
                }

                override fun onBitmapFailed(
                    e: java.lang.Exception?,
                    errorDrawable: Drawable?
                ) {
                    Timber.i("Retro - Lets Go onBitmapFailed $e")
                }

                override fun onPrepareLoad(placeHolderDrawable: Drawable?) {}
            })
    }

    //function to upload an image under the arcades folder on Firebase storage
    fun uploadArcadeImage(arcade: ArcadeModel, callback: (String) -> Unit) {
        val imageUri = arcade.image
        val userid = arcade.uid
        val imageUid = UUID.randomUUID()

        if (imageUri != null && userid != null) {
            val imageRef = storage.child("arcades").child("${imageUid}.jpg")
            val uploadTask: UploadTask = imageRef.putFile(Uri.parse(imageUri))

            uploadTask.addOnSuccessListener { taskSnapshot ->
                taskSnapshot.metadata!!.reference!!.downloadUrl.addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val downloadUrl = task.result.toString()
                        Timber.i("Image uploaded successfully: $downloadUrl")
                        callback(downloadUrl) // Call the callback with the URL
                    }
                }
            }.addOnFailureListener {
                Timber.e("Image upload failed: ${it.message}")
            }
        }
    }
}