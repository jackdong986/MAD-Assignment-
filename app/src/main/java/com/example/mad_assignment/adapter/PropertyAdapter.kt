package com.example.mad_assignment.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.mad_assignment.R
import com.example.mad_assignment.viewModel.Property

class PropertyAdapter(
    private var properties: List<Property>,
    private val onClick: (Property) -> Unit
) : RecyclerView.Adapter<PropertyAdapter.PropertyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PropertyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_property, parent, false)
        return PropertyViewHolder(view)
    }

    override fun onBindViewHolder(holder: PropertyViewHolder, position: Int) {
        holder.bind(properties[position])
    }

    override fun getItemCount(): Int = properties.size

    @SuppressLint("NotifyDataSetChanged")
    fun updateProperties(newProperties: List<Property>) {
        properties = newProperties
        notifyDataSetChanged()
    }

    inner class PropertyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val propertyImage: ImageView = itemView.findViewById(R.id.property_image)
        private val propertyName: TextView = itemView.findViewById(R.id.property_name)
        private val propertyPrice: TextView = itemView.findViewById(R.id.property_price)

        fun bind(propertyDatatype: Property) {
            propertyName.text = propertyDatatype.propertyName
            propertyPrice.text = propertyDatatype.propertyPrice.toString()

            Glide.with(itemView.context)
                .load(propertyDatatype.propertyImage.toBytes())
                .placeholder(R.drawable.icon_image_not_found_free_vector)
                .error(R.drawable.icon_image_not_found_free_vector)
                .into(propertyImage)

            itemView.setOnClickListener {
                onClick(propertyDatatype)
            }
        }
    }
}
