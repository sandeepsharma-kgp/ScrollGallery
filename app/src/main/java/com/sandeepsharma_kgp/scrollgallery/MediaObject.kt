package com.sandeepsharma_kgp.scrollgallery

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize


@Parcelize
data class MediaObject (var title: String?, var media_url: String?, var mediaType: Int?) :  Parcelable