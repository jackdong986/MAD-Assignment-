package com.example.mad_assignment.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.mad_assignment.R

data class Property(val name: String, val price: String, val imageResId: Int, val address: String, val city: String, val state: String, val bathrooms: Int, val bedrooms: Int, val description: String)



class PropertyAdapter(
    private val properties: List<Property>,
    private val onClick: (Property) -> Unit
) : RecyclerView.Adapter<PropertyAdapter.PropertyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PropertyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_property, parent, false)
        return PropertyViewHolder(view)
    }

    override fun onBindViewHolder(holder: PropertyViewHolder, position: Int) {
        val property = properties[position]
        holder.bind(property)
        holder.itemView.setOnClickListener { onClick(property) }
    }

    override fun getItemCount(): Int = properties.size

    class PropertyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val propertyImage: ImageView = itemView.findViewById(R.id.property_image)
        private val propertyName: TextView = itemView.findViewById(R.id.property_name)
        private val propertyPrice: TextView = itemView.findViewById(R.id.property_price)
        private val propertyAddress: TextView = itemView.findViewById(R.id.property_address)
        private val propertyCity: TextView = itemView.findViewById(R.id.property_city)
        private val propertyState: TextView = itemView.findViewById(R.id.property_state)
        private val propertyBathrooms: TextView = itemView.findViewById(R.id.property_bathroom)
        private val propertyBedrooms: TextView = itemView.findViewById(R.id.property_bedroom)
        private val propertyDescription: TextView = itemView.findViewById(R.id.property_description)

        fun bind(property: Property) {
            propertyImage.setImageResource(property.imageResId)
            propertyName.text = property.name
            propertyPrice.text = property.price
            propertyAddress.text = property.address
            propertyCity.text = property.city
            propertyState.text = property.state
            propertyBathrooms.text = property.bathrooms.toString()
            propertyBedrooms.text = property.bedrooms.toString()
            propertyDescription.text = property.description
        }
    }
}