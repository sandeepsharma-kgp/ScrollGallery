package com.sandeepsharma_kgp.scrollgallery

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso


public class MediaAdapter : ListAdapter<MediaObject, MediaAdapter.MediaViewHolder>(DiffCallback){
    companion object DiffCallback : DiffUtil.ItemCallback<MediaObject>() {
        override fun areItemsTheSame(oldItem: MediaObject, newItem: MediaObject): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: MediaObject, newItem: MediaObject): Boolean {
            return oldItem.title == newItem.title
        }
}


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MediaViewHolder {
        return MediaViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.media_item,
                parent,
                false
            )
        )
    }


    override fun onBindViewHolder(holder: MediaViewHolder, position: Int) {
        holder.bindMediaItems(getItem(position))
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