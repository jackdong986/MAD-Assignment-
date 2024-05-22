package com.example.mad_assignment.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.mad_assignment.databinding.ItemHostPropertyBinding
import com.example.mad_assignment.util.setImageBlob
import com.example.mad_assignment.viewModel.Property


class PropertyHostAdapter(
    val fn: (ViewHolder, Property) -> Unit = { _, _ -> }
) : ListAdapter<Property, PropertyHostAdapter.ViewHolder>(DiffCallback){

    companion object DiffCallback : DiffUtil.ItemCallback<Property>() {
        override fun areItemsTheSame(a: Property, b: Property) = a.id == b.id
        override fun areContentsTheSame(a: Property, b: Property) = a == b
    }
    class ViewHolder(val binding: ItemHostPropertyBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ViewHolder(ItemHostPropertyBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val property = getItem(position)

        holder.binding.propertyName.text = property.propertyName
        holder.binding.propertyPrice.text = "RM ${"%.2f".format(property.propertyPrice)}"
        holder.binding.propertyState.text = property.propertyState
        holder.binding.propertyImage.setImageBlob(property.propertyImage)

        fn(holder, property)
    }
}