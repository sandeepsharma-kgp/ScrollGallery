package com.sandeepsharma_kgp.scrollgallery

import androidx.databinding.BindingAdapter


@BindingAdapter("mediaListData")
fun bindMediaRecyclerView(recyclerView: MediaRecyclerView,data: List<MediaObject>) {
    if(recyclerView.adapter is MediaAdapter)
        (recyclerView.adapter as MediaAdapter).submitList(data)
}