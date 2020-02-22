package com.sandeepsharma_kgp.scrollgallery

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Rect
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sandeepsharma_kgp.scrollgallery.databinding.MainActivityBinding

@Suppress("DEPRECATED_IDENTITY_EQUALS")
class MainActivity : AppCompatActivity() {

    private val MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE: Int = 1
    lateinit var recyclerView: MediaRecyclerView
    private lateinit var mainViewModel: MainViewModel
    private lateinit var binding: MainActivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.main_activity)
        if (ContextCompat.checkSelfPermission(
                this@MainActivity,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) !== PackageManager.PERMISSION_GRANTED
        )
                ActivityCompat.requestPermissions(
                    this@MainActivity,
                    arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                    MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE
                )
        mainViewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)
    }

    override fun onResume() {
        super.onResume()
        initRecyclerView()
    }

    private fun initRecyclerView() {
        recyclerView = binding.galleryRecyclerView
        recyclerView.adapter = MediaAdapter()
        binding.mainViewModel = mainViewModel
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

        mainViewModel.mediaObjectList.observe(this, Observer{
            recyclerView.setMediaObjects(it)
        })
    }

    override fun onDestroy() {
        recyclerView.releasePlayer()
        super.onDestroy()
    }
}
