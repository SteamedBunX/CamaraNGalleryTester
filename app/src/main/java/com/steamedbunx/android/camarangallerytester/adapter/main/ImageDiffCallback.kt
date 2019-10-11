package com.steamedbunx.android.camarangallerytester.adapter.main

import androidx.recyclerview.widget.DiffUtil
import com.steamedbunx.android.camarangallerytester.data.ImageStored


class ImageDiffCallback: DiffUtil.ItemCallback<ImageStored>(){
    override fun areItemsTheSame(oldItem: ImageStored, newItem: ImageStored): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: ImageStored, newItem: ImageStored): Boolean {
        return oldItem.imageId == newItem.imageId
    }

}