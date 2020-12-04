package com.example.lyngua.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.anychart.AnyChart
import com.anychart.AnyChartView
import com.anychart.chart.common.dataentry.DataEntry
import com.anychart.chart.common.dataentry.ValueDataEntry
import com.anychart.chart.common.listener.Event
import com.anychart.chart.common.listener.ListenersInterface
import com.anychart.enums.Align
import com.anychart.enums.LegendLayout
import com.example.lyngua.R
import com.example.lyngua.controllers.CategoryController
import com.example.lyngua.models.ResultLogs.ResultLogs
import kotlinx.android.synthetic.main.fragment_stats.*

/*
Source for the pie chart library: https://github.com/AnyChart/AnyChart-Android/blob/master/sample/src/main/java/com/anychart/sample/charts/PieChartActivity.java
 */
class Stats : Fragment() {
    lateinit var categoryController: CategoryController
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_stats, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        categoryController = CategoryController(requireContext())

        stats_switch.setCheckedPosition(0)
        showStatsWeek(getTimeFrame(0))

    }

    private fun showStatsWeek(timeFrame: Long){
        var results: List<ResultLogs>? = categoryController.getResults(timeFrame)
        var str = ""
        if(results != null) {
            for(result in results) {
                str += result.catName.toString() + "\n"
            }
            stats_text_view.text = str
            pieChart()
        }
    }


    private fun pieChart(){

        val anyChartView: AnyChartView = any_chart_view
        anyChartView.setProgressBar(progress_bar)

        val pie = AnyChart.pie()

        pie.setOnClickListener(object : ListenersInterface.OnClickListener(arrayOf("x", "value")) {
            override fun onClick(event: Event) {
                Toast.makeText(
                    requireContext(),
                    event.getData().get("x").toString() + ":" + event.getData().get("value"),
                    Toast.LENGTH_SHORT
                ).show()
            }
        })

        val data: MutableList<DataEntry> = ArrayList()
        data.add(ValueDataEntry("Apples", 6371664))
        data.add(ValueDataEntry("Pears", 789622))
        data.add(ValueDataEntry("Bananas", 7216301))
        data.add(ValueDataEntry("Grapes", 1486621))
        data.add(ValueDataEntry("Oranges", 1200000))

        pie.data(data)

        pie.title("Breakdown of Words Practiced")


        anyChartView.setChart(pie)
    }

    private fun getTimeFrame(position: Int): Long{
        return if(position == 0){
            //stats for one week:
            60*60*24*7
        }else if(position == 1){
            //stats for one month
            60*60*24*30
        }else{
            //stats for one year
            60*60*24*365
        }

    }
}