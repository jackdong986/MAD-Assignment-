package com.example.mad_assignment.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.mad_assignment.databinding.ItemHostPropertyBinding
import com.example.mad_assignment.databinding.ItemHostRentingBinding
import com.example.mad_assignment.util.setImageBlob
import com.example.mad_assignment.viewModel.PROPERTIES
import com.example.mad_assignment.viewModel.Property
import com.example.mad_assignment.viewModel.Renting
import java.text.SimpleDateFormat
import java.util.Locale

class RentingHostAdapter(
    val fn: (ViewHolder, Renting) -> Unit = { _, _ -> }
) : ListAdapter<Renting, RentingHostAdapter.ViewHolder>(DiffCallback){

    companion object DiffCallback : DiffUtil.ItemCallback<Renting>() {
        override fun areItemsTheSame(a: Renting, b: Renting) = a.id == b.id
        override fun areContentsTheSame(a: Renting, b: Renting) = a == b
    }
    class ViewHolder(val binding: ItemHostRentingBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ViewHolder(ItemHostRentingBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val renting = getItem(position)

        holder.binding.rentingHostID.text = renting.id
        holder.binding.rentingHostPaymentStatus.text = renting.paymentStatus
        val propertyID = renting.propertyId
        // find property Image, name, address from firebase based on property ID
        PROPERTIES.document(propertyID).get().addOnSuccessListener { document ->
            if (document != null) {
                val property = document.toObject(Property::class.java)
                if (property != null) {
                    // Set property image, and name
                    holder.binding.imgPropertyRenting.setImageBlob(property.propertyImage)
                    holder.binding.rentingPropertyName.text = property.propertyName
                }
            }
        }

        holder.binding.rentingTotalAmount.text = "RM ${renting.totalAmount}"
        holder.binding.rentingTotalMonth.text = "(${renting.totalMonth} month(s))"
        // format date to (dd MM yyyy), EG 21 June 2021
        val dateFormat = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault())
        holder.binding.rentingStartDate.text = dateFormat.format(renting.rentingStartDate)
        holder.binding.rentingEndDate.text = dateFormat.format(renting.rentingEndDate)


        fn(holder, renting)
    }
}