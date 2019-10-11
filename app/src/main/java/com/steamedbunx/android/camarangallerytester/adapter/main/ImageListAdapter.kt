package com.steamedbunx.android.camarangallerytester.adapter.main

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.steamedbunx.android.camarangallerytester.data.ImageStored
import com.steamedbunx.android.camarangallerytester.databinding.ListItemImageStoredBinding

class ImageListAdapter(val clickListner: ImageListListeners) :
    ListAdapter<ImageStored, ImageListAdapter.ImageStoredViewHolder>(ImageDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageStoredViewHolder {
        return ImageStoredViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ImageStoredViewHolder, position: Int) {
        holder.bind(getItem(position), clickListner)
    }

    class ImageStoredViewHolder private constructor(val binding: ListItemImageStoredBinding) :
        RecyclerView.ViewHolder(binding.root) {
        companion object{
            fun from(parent:ViewGroup): ImageStoredViewHolder{
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ListItemImageStoredBinding.inflate(layoutInflater,parent,false)
                return ImageStoredViewHolder(binding)
            }
        }

        fun bind(item: ImageStored, clickListners: ImageListListeners){
            binding.imageInfo = item
            binding.clickListners = clickListners
            binding.executePendingBindings()
        }
    }
}

interface ImageListListeners{
    fun buttonDeleteOnClickListener(item: ImageStored)
    fun buttonCropOnClickListener()
}
