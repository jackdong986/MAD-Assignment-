package com.example.mad_assignment.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.anychart.AnyChart
import com.anychart.AnyChartView
import com.anychart.chart.common.dataentry.DataEntry
import com.anychart.chart.common.dataentry.ValueDataEntry
import com.example.mad_assignment.databinding.FragmentHostDashboardBinding
import com.example.mad_assignment.util.toast
import com.example.mad_assignment.viewModel.RESTORE


class HostDashboardFragment : Fragment() {

    private lateinit var binding: FragmentHostDashboardBinding
    private val nav by lazy { findNavController() }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentHostDashboardBinding.inflate(inflater, container, false)

        binding.restore.setOnClickListener { restore() }

        val pie = AnyChart.pie()

        val data: MutableList<DataEntry> = ArrayList()
        data.add(ValueDataEntry("John", 10000))
        data.add(ValueDataEntry("Jake", 12000))
        data.add(ValueDataEntry("Peter", 18000))

        pie.data(data)
        var credits = pie.credits();
        credits.enabled(false);

        val anyChartView = binding.anyChartView
        anyChartView.setChart(pie)
        return binding.root
    }

    private fun restore() {
        RESTORE(requireContext())
        toast("Done")
    }

}