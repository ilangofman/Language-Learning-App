package com.example.lyngua.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import androidx.fragment.app.Fragment
import belka.us.androidtoggleswitch.widgets.BaseToggleSwitch
import com.anychart.AnyChart
import com.anychart.AnyChartView
import com.anychart.chart.common.dataentry.DataEntry
import com.anychart.chart.common.dataentry.ValueDataEntry
import com.anychart.palettes.RangeColors
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


        stats_switch.checkedTogglePosition = 0
        showStats(getTimeFrame(0))

        stats_switch.setOnToggleSwitchChangeListener { position, isChecked ->
            val pos = stats_switch.getCheckedTogglePosition()
            if(pos == 0) showStats(getTimeFrame(0))
            else if(pos == 1)   showStats(getTimeFrame(1))
            else showStats(getTimeFrame(2))
        }


//
//        private fun ToggleSwitch.onChangeListener(value: () -> Unit) {
//            val pos = stats_switch.getCheckedPosition()
//            if(pos == 0) showStats(getTimeFrame(0))
//            else if(pos == 0)   showStats(getTimeFrame(1))
//            else showStats(getTimeFrame(2))
//        }
//
//        {
//            val pos = stats_switch.getCheckedPosition()
//            if(pos == 0) showStats(getTimeFrame(0))
//            else if(pos == 0)   showStats(getTimeFrame(1))
//            else showStats(getTimeFrame(2))
//        }

    }

    private fun showStats(timeFrame: Long){
        var results: List<ResultLogs>? = categoryController.getResults(timeFrame)
        var str = ""
        if(results != null) {
            print("IALNANAN" + timeFrame)
            pieChart(results)
        }
    }


    private fun pieChart(results: List<ResultLogs>){

        val anyChartView: AnyChartView = any_chart_view

        anyChartView.setProgressBar(progress_bar)

        val pie = AnyChart.pie()



        var categoryResults = results.groupBy { it.catId }
        val data: MutableList<DataEntry> = ArrayList()

        categoryResults.forEach {
            var total = 0
            it.value.map{ cat -> total += cat.numQuestions}
            println("Group b=${it.key}: ${it.value}, total ${total}")
            data.add(ValueDataEntry(it.value[0].catName, total))

        }

        val palette = RangeColors.instantiate()
        palette.items("#692fa8", "#d9c7ed")
        palette.count(categoryResults.size)
        pie.palette(palette)

        pie.legend().enabled(false)
        pie.data(data)

//        pie.title("Breakdown of Words Practiced")


        anyChartView.setChart(pie)
    }

    private fun getTimeFrame(position: Int): Long{
        val now = System.currentTimeMillis()/1000

        return if(position == 0){
            //stats for one week:
            now - 60*60*24*7
        }else if(position == 1){
            //stats for one month
            now - 60*60*24*30
        }else{
            //stats for one year
            now - 60*60*24*365
        }

    }



}

