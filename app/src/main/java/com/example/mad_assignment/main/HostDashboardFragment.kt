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
import com.example.mad_assignment.viewModel.RESTORE
import com.example.mad_assignment.viewModel.RentingVM


class HostDashboardFragment : Fragment() {

    private lateinit var binding: FragmentHostDashboardBinding
    private val rentingVM: RentingVM by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHostDashboardBinding.inflate(inflater, container, false)


        // Observe the LiveData
        rentingVM.getRentingLD().observe(viewLifecycleOwner, Observer { rentingList ->
            val totalRentingAmount = rentingList.sumOf { it.totalAmount }
            binding.txtTotalRentingAmountForHost.text = "RM ${"%.2f".format(totalRentingAmount)}"

            val totalRenting = rentingList.count { it.paymentStatus == "Success" }
            binding.txtTotalRenting.text = "$totalRenting"

            val averageRentingAmount = if (totalRenting > 0) totalRentingAmount / totalRenting else 0.0
            binding.txtTotalAverageRentingForHost.text = "RM ${"%.2f".format(averageRentingAmount)}"
        })




        // Initialize and set up line chart
        val lineChartView: AnyChartView = binding.lineChart
        val lineChart: Cartesian = AnyChart.line()

        lineChart.title("Trend of Sales")
        lineChart.yAxis(0).title("Number of Sales")

        val lineData: MutableList<DataEntry> = ArrayList()
        lineData.add(CustomDataEntry("Jan", 1000))
        lineData.add(CustomDataEntry("Feb", 1200))
        lineData.add(CustomDataEntry("Mar", 800))
        lineData.add(CustomDataEntry("Apr", 1500))
        lineData.add(CustomDataEntry("May", 900))
        lineData.add(CustomDataEntry("Jun", 1100))
        lineData.add(CustomDataEntry("Jul", 1300))
        lineData.add(CustomDataEntry("Aug", 1400))
        lineData.add(CustomDataEntry("Sep", 1000))
        lineData.add(CustomDataEntry("Oct", 1600))
        lineData.add(CustomDataEntry("Nov", 1100))
        lineData.add(CustomDataEntry("Dec", 1200))

        val lineSet = com.anychart.data.Set.instantiate()
        lineSet.data(lineData)
        val lineMapping = lineSet.mapAs("{ x: 'x', value: 'value' }")

        val lineSeries = lineChart.line(lineMapping)
        lineSeries.name("Sales")

        lineChartView.setChart(lineChart)




        return binding.root
    }

    private class CustomDataEntry(x: String?, value: Number?) : ValueDataEntry(x, value)



    private fun restore() {
        RESTORE(requireContext())
        toast("Done")
    }

}