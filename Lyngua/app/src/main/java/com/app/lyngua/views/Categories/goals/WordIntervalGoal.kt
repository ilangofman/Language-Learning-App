package com.app.lyngua.views.Categories.goals

import android.app.AlertDialog
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.app.lyngua.R
import com.app.lyngua.controllers.CategoryController
import com.app.lyngua.controllers.notifications.AlarmService
import com.app.lyngua.models.goals.Goal
import com.app.lyngua.views.Categories.UpdateCategory.SwitchType.SWITCH_OFF
import com.app.lyngua.views.Categories.UpdateCategory.SwitchType.SWITCH_ON
import com.app.lyngua.views.Categories.UpdateCategoryArgs
import java.util.*
import kotlinx.android.synthetic.main.fragment_word_interval.*
import kotlinx.android.synthetic.main.fragment_word_interval.view.*
import kotlin.collections.ArrayList

    class WordIntervalGoal(arg: UpdateCategoryArgs) : Fragment() {

    private lateinit var categoryController: CategoryController
    private val args = arg

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_word_interval, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val myCalendar = Calendar.getInstance()
        var timeFrameFlag: Int = SWITCH_OFF
        var notificationFlag = SWITCH_OFF
        var goalType: Int = SWITCH_OFF
        var wordGoalCount = SWITCH_OFF

        categoryController = CategoryController(requireContext())

        if (args.categoryChosen.goal.goalType == SWITCH_ON) {
            setSelectedOption(tv_goal_option_one)
        }

        //Sets the word count for goal based on last update of category
        if (args.categoryChosen.goal.totalNumWords != 0) {
            view.words_goal_count_text.setText(args.categoryChosen.goal.totalNumWords.toString())
        }

        //If notifications were enabled before, ensures the box is checked
        if (args.categoryChosen.goal.notificationFlag == 1) {
            view.notification_checkbox.isChecked = true
            notificationFlag = SWITCH_ON
        }

        val spinner: Spinner = view.findViewById(R.id.set_goals_spn)

        //Create the options for the spinner
        var options: MutableList<String> = ArrayList()
        options.add(0, "No Goal")
        options.add(1, "Day")
        options.add(2, "Week")
        options.add(3, "Month")

        //Create array adapter to display the options list with the spinner
        val arrayAdapter = ArrayAdapter<String>(
            requireActivity().applicationContext,
            android.R.layout.simple_list_item_1,
            options
        )

        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = arrayAdapter

        //If a previous time frame was already set, update spinner to that option
        if (args.categoryChosen.goal.goalType != 0) {
            spinner.setSelection(args.categoryChosen.goal.timeFrame)
        }

        // Create spinner listeners for goal time frame
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                if (parent!!.getItemAtPosition(position) == "No Goal") {
                    timeFrameFlag = SWITCH_OFF
                    goalType = SWITCH_OFF
                    notificationFlag = SWITCH_OFF
                    wordGoalCount = SWITCH_OFF

                } else {

                    //Timeframe based on the spinner selected
                    when {
                        parent.getItemAtPosition(position) == "Day" -> {
                            timeFrameFlag = 1
                        }
                        parent.getItemAtPosition(position) == "Week" -> {
                            timeFrameFlag = 2
                        }
                        parent.getItemAtPosition(position) == "Month" -> {
                            timeFrameFlag = 3
                        }
                    }
                    goalType = SWITCH_ON
                }
            }
        }

        //View for checkbox for notification reminder
        val checkBox = view.findViewById(R.id.notification_checkbox) as CheckBox
        checkBox.setOnCheckedChangeListener { compoundButton, b ->
            if (b) {
                notificationFlag = SWITCH_ON
            } else {
                notificationFlag = SWITCH_OFF
            }

        }

        //On Click listener for the update category button
        view.update_category_btn.setOnClickListener {

            if(goalType == SWITCH_ON) {
                if (view.words_goal_count_text.text.toString().isEmpty()) {
                    wordGoalCount = 50
                } else {
                    wordGoalCount = Integer.parseInt(view.words_goal_count_text.text.toString())
                }
            }

            //Based on which spinner was chosen, detail the time for when the goal should be complete
            when (timeFrameFlag) {
                0 -> args.categoryChosen.goal.cancelAlarms(requireContext(), args.categoryChosen)
                1 -> myCalendar.add(Calendar.DAY_OF_MONTH, 1)
                2 -> myCalendar.add(Calendar.DAY_OF_MONTH, 7)
                3 -> myCalendar.add(Calendar.MINUTE, 2)
            }

            //Creates a goal object based on the options chosen from updating to be put into the database
            val goal: Goal = Goal(
                myCalendar,
                timeFrameFlag,
                notificationFlag,
                goalType,
                0,
                wordGoalCount,
                0.0,
                0.0
            )

            val result = categoryController.updateCategory(
                args.categoryChosen.id,
                args.categoryChosen.name,
                args.categoryChosen.numWords,
                args.categoryChosen.wordsList,
                args.categoryChosen.sessionNumber,
                goal
            )

            if (timeFrameFlag != SWITCH_OFF) {
                //Creates an alarmservice to run in the background
                val alarm =
                    AlarmService(requireActivity().applicationContext, args.categoryChosen, goal)

                alarm.startAlarm()
            }
            if (notificationFlag == SWITCH_OFF){
                args.categoryChosen.goal.cancelAlarms(requireContext(), args.categoryChosen)
            }

            if (result) {
                findNavController().navigate(R.id.action_updateCategoryFragment_to_practice)
            }

        }

        //On Click listener for the delete category button
        view.delete_category_btn.setOnClickListener {
            val confirmation = AlertDialog.Builder(requireContext(), R.style.AlertDialogCustom)
            confirmation.setTitle("Delete?")
            confirmation.setMessage("Are you sure you would like to delete this category?")
            confirmation.setPositiveButton("Delete") { _, _ ->

                args.categoryChosen.goal.cancelAlarms(requireContext(), args.categoryChosen)
                categoryController.deleteCategory(args.categoryChosen)
                Toast.makeText(requireContext(), "Delete Success", Toast.LENGTH_SHORT).show()
                findNavController().navigate(R.id.action_updateCategoryFragment_to_practice)
            }
            confirmation.setNegativeButton("Cancel") { _, _ -> }
            confirmation.create().show()

        }

    }

    private fun setSelectedOption(view: TextView) {
        view.setTextColor(Color.parseColor("#FFFFFF"))
        view.background =
            ContextCompat.getDrawable(requireContext(), R.drawable.selected_goal_background_color)
    }

}
