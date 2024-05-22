package com.example.mad_assignment.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.anychart.AnyChart
import com.anychart.AnyChartView
import com.anychart.chart.common.dataentry.DataEntry
import com.anychart.chart.common.dataentry.ValueDataEntry
import com.anychart.charts.Cartesian
import com.anychart.core.cartesian.series.Column
import com.anychart.enums.Anchor
import com.anychart.enums.HoverMode
import com.anychart.enums.Position
import com.anychart.enums.TooltipPositionMode
import com.example.mad_assignment.databinding.FragmentHostDashboardBinding
import com.example.mad_assignment.util.toast
import com.example.mad_assignment.viewModel.RENTING
import com.example.mad_assignment.viewModel.RESTORE
import com.example.mad_assignment.viewModel.Renting
import com.example.mad_assignment.viewModel.RentingVM
import com.google.firebase.auth.FirebaseAuth
import java.util.Calendar


class HostDashboardFragment : Fragment() {

    private lateinit var binding: FragmentHostDashboardBinding
    private val rentingVM: RentingVM by activityViewModels()
    private var currentUserId = "";


    private lateinit var lineChartView: AnyChartView
    private lateinit var lineChart: Cartesian

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHostDashboardBinding.inflate(inflater, container, false)

//        binding.restore.setOnClickListener { restore() }

        val auth = FirebaseAuth.getInstance()
        val currentUser = auth.currentUser
        currentUserId = currentUser?.uid ?: ""

        if(currentUserId.isEmpty()) {
            toast("No user found")
            return binding.root
        }

        // Initialize chart
        lineChartView = binding.lineChart
        lineChart = AnyChart.line()
        lineChart.credits().enabled(false)
        lineChart.title("Trend of Sales")
        lineChart.yAxis(0).title("Total Sales (RM)")
        lineChartView.setChart(lineChart)


        // Observe the LiveData
        rentingVM.getRentingLD().observe(viewLifecycleOwner, Observer { rentingList ->
            val filteredList = rentingList.filter { it.hostId == currentUserId }

            updateDashboard(filteredList)
            updateLineChart(filteredList)
        })





        return binding.root
    }

    private fun updateDashboard(rentingList: List<Renting>) {
        val totalRentingAmount = rentingList.sumOf { it.totalAmount }
        binding.txtTotalRentingAmountForHost.text = "RM ${"%.2f".format(totalRentingAmount)}"

        val totalRenting = rentingList.count { it.paymentStatus == "Success" }
        binding.txtTotalRenting.text = "$totalRenting"

        val averageRentingAmount = if (totalRenting > 0) totalRentingAmount / totalRenting else 0.0
        binding.txtTotalAverageRentingForHost.text = "RM ${"%.2f".format(averageRentingAmount)}"
    }

    private fun updateLineChart(rentingList: List<Renting>) {
        val monthlyTotals = DoubleArray(12) { 0.0 }
        val calendar = Calendar.getInstance()
        val currentYear = calendar.get(Calendar.YEAR)

        rentingList.forEach { renting ->
            calendar.time = renting.createdAt
            if (calendar.get(Calendar.YEAR) == currentYear) {
                val month = calendar.get(Calendar.MONTH) // 0-based month
                monthlyTotals[month] += renting.totalAmount
            }
        }

        val lineData: MutableList<DataEntry> = ArrayList()
        val months = arrayOf("Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec")
        for (i in months.indices) {
            lineData.add(CustomDataEntry(months[i], monthlyTotals[i]))
        }

        val lineSet = com.anychart.data.Set.instantiate()
        lineSet.data(lineData)
        val lineMapping = lineSet.mapAs("{ x: 'x', value: 'value' }")

        // Clear previous series and add new series
        lineChart.removeAllSeries()
        lineChart.line(lineMapping).name("Sales")
    }



    private class CustomDataEntry(x: String?, value: Number?) : ValueDataEntry(x, value)



    private fun restore() {
        RESTORE(requireContext())
        toast("Done")
    }

}