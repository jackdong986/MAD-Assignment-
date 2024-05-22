package com.example.mad_assignment.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.mad_assignment.databinding.ItemImageSliderBinding

class ImageSliderAdapter(
    private val images: List<Int>,
    private val onImageClick: (Int) -> Unit // Click listener callback
) : RecyclerView.Adapter<ImageSliderAdapter.ImageSliderViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageSliderViewHolder {
        val binding = ItemImageSliderBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ImageSliderViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ImageSliderViewHolder, position: Int) {
        val imageRes = images[position]
        holder.bind(imageRes)
    }

    override fun getItemCount(): Int {
        return images.size
    }

    inner class ImageSliderViewHolder(private val binding: ItemImageSliderBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(imageRes: Int) {
            binding.imageView.setImageResource(imageRes)
            binding.imageView.setOnClickListener {
                onImageClick(imageRes) // Trigger the click listener with the image resource ID
            }
        }
    }
}
