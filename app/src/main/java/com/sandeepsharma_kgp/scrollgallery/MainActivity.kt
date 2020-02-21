package com.sandeepsharma_kgp.scrollgallery

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Rect
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.loader.content.CursorLoader
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

@Suppress("DEPRECATED_IDENTITY_EQUALS")
class MainActivity : AppCompatActivity() {

    private val MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE: Int = 1
    lateinit var recyclerView: MediaRecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        if (ContextCompat.checkSelfPermission(
                this@MainActivity,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) !== PackageManager.PERMISSION_GRANTED
        )
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                ActivityCompat.requestPermissions(
                    this@MainActivity,
                    arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                    MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE
                )
            }
        recyclerView = findViewById(R.id.galleryRecyclerView)
    }

    override fun onResume() {
        super.onResume()
        initRecyclerView()
    }

    private fun initRecyclerView() {
        val linearLayoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = linearLayoutManager
        recyclerView.addItemDecoration(object : RecyclerView.ItemDecoration() {
            override fun getItemOffsets(
                outRect: Rect,
                view: View,
                parent: RecyclerView,
                state: RecyclerView.State
            ) {
                outRect.top = 10
                super.getItemOffsets(outRect, view, parent, state)
            }
        })

        val projection = arrayOf(
            MediaStore.Files.FileColumns._ID,
            MediaStore.Files.FileColumns.DATA,
            MediaStore.Files.FileColumns.DATE_ADDED,
            MediaStore.Files.FileColumns.MEDIA_TYPE,
            MediaStore.Files.FileColumns.TITLE
        )

// Return only video and image metadata.
        val selection = (MediaStore.Files.FileColumns.MEDIA_TYPE + "="
                + MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE
                + " OR "
                + MediaStore.Files.FileColumns.MEDIA_TYPE + "="
                + MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO)

        val queryUri = MediaStore.Files.getContentUri("external")

        val cursorLoader = CursorLoader(
            this,
            queryUri,
            projection,
            selection,
            null, // Selection args (none).
            MediaStore.Files.FileColumns.DATE_ADDED + " ASC" // Sort order.
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
        } catch (e: Exception) {
            e.printStackTrace()
        }

        recyclerView.setMediaObjects(mediaObjects)
        recyclerView.adapter = MediaAdapter(mediaObjects)
    }

    override fun onDestroy() {
        recyclerView.releasePlayer()
        super.onDestroy()
    }
}
