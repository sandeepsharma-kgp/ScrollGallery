package com.sandeepsharma_kgp.scrollgallery

import android.app.Application
import android.provider.MediaStore
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.loader.content.CursorLoader

class MainViewModel(application: Application) : AndroidViewModel(application) {
    private val _mediaObjectList = MutableLiveData<List<MediaObject>>()
    val mediaObjectList: LiveData<List<MediaObject>>
        get() = _mediaObjectList

    init {
        setMediaList()
    }

    fun setMediaList() {
        val projection = arrayOf(
            MediaStore.Files.FileColumns._ID,
            MediaStore.Files.FileColumns.DATA,
            MediaStore.Files.FileColumns.DATE_ADDED,
            MediaStore.Files.FileColumns.MEDIA_TYPE,
            MediaStore.Files.FileColumns.TITLE
        )

        val selection = (MediaStore.Files.FileColumns.MEDIA_TYPE + "="
                + MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE
                + " OR "
                + MediaStore.Files.FileColumns.MEDIA_TYPE + "="
                + MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO)

        val queryUri = MediaStore.Files.getContentUri("external")

        val cursorLoader = CursorLoader(
            getApplication(),
            queryUri,
            projection,
            selection,
            null, // Selection args (none).
            MediaStore.Files.FileColumns.DATE_ADDED + " DESC" // Sort order.
        )

        val cursor = cursorLoader.loadInBackground()
        val mediaObjects = ArrayList<MediaObject>()
        var count: Int = 0
        try {
            cursor?.let {
                it.moveToFirst()
                do {
//                    if(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.MEDIA_TYPE))
//                            .toInt() == 1)
//                        continue
                    var mediaObject: MediaObject
                    count++
                    if (count % 5 == 0) {
                        mediaObject = MediaObject(null, "https://source.unsplash.com/random", 4)
                    } else {
                        mediaObject =
                            MediaObject(
                                cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.TITLE)),
                                cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.DATA)),
                                null
                            )
                        mediaObject.mediaType =
                            cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.MEDIA_TYPE))
                                .toInt()
                    }
                    mediaObjects.add(mediaObject)
                } while (it.moveToNext())
                cursor.close()
            }
            _mediaObjectList.value = mediaObjects
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}

