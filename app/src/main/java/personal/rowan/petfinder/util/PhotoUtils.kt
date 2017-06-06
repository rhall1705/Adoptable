package personal.rowan.petfinder.util

import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Environment
import android.widget.Toast
import com.squareup.picasso.Picasso
import com.squareup.picasso.Target
import personal.rowan.petfinder.R
import java.io.File
import java.io.FileOutputStream

/**
 * Created by Rowan Hall
 */
object PhotoUtils {

    fun imageDownload(context: Activity, url: String) {
        Picasso.with(context)
                .load(url)
                .into(getTarget(url.replace("/", "_"), context))
    }

    private fun getTarget(url: String, context: Activity): Target {
        val target = object : Target {

            override fun onBitmapLoaded(bitmap: Bitmap, from: Picasso.LoadedFrom) {
                Thread(Runnable {
                    val file = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), url)
                    try {
                        if (!file.createNewFile()) {
                            context.runOnUiThread {
                                Toast.makeText(context, R.string.pet_detail_photo_download_duplicate, Toast.LENGTH_SHORT).show()
                            }
                            return@Runnable
                        }
                        val ostream = FileOutputStream(file)
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 80, ostream)
                        ostream.flush()
                        ostream.close()
                        context.runOnUiThread {
                            Toast.makeText(context, R.string.pet_detail_photo_download_successful, Toast.LENGTH_SHORT).show()
                        }
                    } catch (e: Throwable) {
                        context.runOnUiThread {
                            Toast.makeText(context, R.string.pet_detail_photo_download_failure, Toast.LENGTH_SHORT).show()
                        }
                    }
                }).start()

            }

            override fun onBitmapFailed(errorDrawable: Drawable) {
                Toast.makeText(context, R.string.pet_detail_photo_download_failure, Toast.LENGTH_SHORT).show()
            }

            override fun onPrepareLoad(placeHolderDrawable: Drawable) {

            }
        }
        return target
    }

}