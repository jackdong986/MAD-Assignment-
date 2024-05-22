package com.example.mad_assignment.payment

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.mad_assignment.R
import com.example.mad_assignment.viewModel.Property
import com.example.mad_assignment.viewModel.Renting

class PaymentHistoryAdapter(
    private var rentingList: List<Renting>,
    private var propertiesMap: Map<String, Property>
) : RecyclerView.Adapter<PaymentHistoryAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val propertyImage: ImageView = itemView.findViewById(R.id.property_image)
        val propertyName: TextView = itemView.findViewById(R.id.property_name)
        val paymentStatus: TextView = itemView.findViewById(R.id.payment_status)
        val totalAmount: TextView = itemView.findViewById(R.id.total_amount)
        val totalMonth: TextView = itemView.findViewById(R.id.total_month)
        val createdat: TextView = itemView.findViewById(R.id.created_At)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_payment_history, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val renting = rentingList[position]
        val property = propertiesMap[renting.propertyId]

        if (property != null) {
            Glide.with(holder.itemView.context)
                .load(property.propertyImage.toBytes())
                .placeholder(R.drawable.icon_image_not_found_free_vector)
                .into(holder.propertyImage)

            holder.propertyName.text = property.propertyName
        }

        holder.paymentStatus.text = renting.paymentStatus
        holder.totalAmount.text = "Total Amount: ${renting.totalAmount}"
        holder.totalMonth.text = "Total Months: ${renting.totalMonth}"
        holder.createdat.text = "Created At: ${renting.createdAt}"
    }

    override fun getItemCount(): Int = rentingList.size

    fun updateData(newRentingList: List<Renting>, newPropertiesMap: Map<String, Property>) {
        rentingList = newRentingList
        propertiesMap = newPropertiesMap
        notifyDataSetChanged()
    }
}
