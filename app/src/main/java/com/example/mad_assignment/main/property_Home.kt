package com.example.mad_assignment.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mad_assignment.R
import com.example.mad_assignment.databinding.FragmentPropertyHomeBinding

class property_Home : Fragment() {
    private lateinit var binding: FragmentPropertyHomeBinding
    private val nav by lazy { findNavController() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPropertyHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recyclerView = binding.recyclerView
        recyclerView.layoutManager = GridLayoutManager(requireContext(), 2)

        val properties = listOf(
            Property("Property 1", "$1000/month", R.drawable.property1),
            Property("Property 2", "$1200/month", R.drawable.property1),
            // Add more properties here
        )
        val adapter = PropertyAdapter(properties) { property ->
            val action = property_HomeDirections.actionPropertyHomeToPropertyDetails(
                propertyName = property.name,
                propertyPrice = property.price,
                propertyImageResId = property.imageResId
            )
            nav.navigate(action)
        }
        recyclerView.adapter = adapter
    }
}

data class Property(val name: String, val price: String, val imageResId: Int)

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

        fun bind(property: Property) {
            propertyImage.setImageResource(property.imageResId)
            propertyName.text = property.name
            propertyPrice.text = property.price
        }
    }
}
