package com.sandeepsharma_kgp.scrollgallery

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.util.Log
import android.widget.Toast
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso


public class MediaAdapter(var mediaObjects: ArrayList<MediaObject>) :
    RecyclerView.Adapter<ViewHolder>(), View.OnClickListener {
    override fun onClick(v: View?) {

    }

    fun updateData(mediaObjects: ArrayList<MediaObject>) {
        this.mediaObjects = mediaObjects
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return MediaViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.media_item,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return mediaObjects.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        (holder as MediaViewHolder).bindMediaItems(mediaObjects[position])
    }

    class MediaViewHolder(itemView: View) : ViewHolder(itemView) {
        val titleText = itemView.findViewById(R.id.title) as TextView
        val imageView = itemView.findViewById(R.id.mediaImage) as ImageView
        val volumeControl = itemView.findViewById(R.id.volume_control) as ImageView
        val progressBar = itemView.findViewById(R.id.progressBar) as ProgressBar

        fun bindMediaItems(mediaObject: MediaObject) {
            itemView.setTag(this)
            if (mediaObject.mediaType == 4) {
                titleText.setText("REPEATING IMAGE")
                mediaObject.media_url?.let {
                    Picasso.with(imageView.context)
                        .load(it)
                        .placeholder(R.drawable.loading_animation)
                        .error(R.drawable.ic_broken_image)
                        .into(imageView, object: Callback{
                            override fun onSuccess() {

                            }

                            override fun onError() {
                               Toast.makeText(itemView.context,"An error occured",Toast.LENGTH_SHORT).show()
                            }

                        })
                }
            } else {
                titleText.setText(mediaObject.title)
                Glide.with(itemView)
                    .load(mediaObject.media_url)
                    .into(imageView)
            }

        }
    }
}