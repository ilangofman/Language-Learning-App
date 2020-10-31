package com.example.lyngua.views.Categories


import android.app.AlertDialog
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.lyngua.R
import com.example.lyngua.controllers.CategoryController
import com.example.lyngua.controllers.notifications.AlarmService
import com.example.lyngua.models.goals.Goal
import kotlinx.android.synthetic.main.fragment_update_category.*
import kotlinx.android.synthetic.main.fragment_update_category.view.*
import java.util.*
import kotlin.collections.ArrayList


class UpdateCategory : Fragment() {

    private lateinit var categoryController: CategoryController
    private val args by navArgs<UpdateCategoryArgs>()

    //TODO need to update for other goal type
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_update_category, container, false)

        val myCalendar = Calendar.getInstance()
        var timeFrameFlag: Int = -1
        var notificationFlag: Int = 0
        var goalType: Int = -1 //TODO this goaltype variable is currently not being used

        categoryController = CategoryController(requireContext())

        /*view.tv_goal_option_one.setOnClickListener{
            setDefaultOption(tv_goal_option_two)
            setSelectedOption(tv_goal_option_one)
            view.text_practice_var.setText("Number of words")
            goalType = 0
        }

        view.tv_goal_option_two.setOnClickListener{
            setDefaultOption(tv_goal_option_one)
            setSelectedOption(tv_goal_option_two)
            view.text_practice_var.setText("Amount of time within time frame")
            goalType = 1
        }*/

        //Sets the word count for goal based on last update of category
        if (args.categoryChosen.goal.totalNumWords != -1) {
            view._words_goal_count_text.setText(args.categoryChosen.goal.totalNumWords.toString())
        }

        //If notifications were enabled before, ensures the box is checked
        if (args.categoryChosen.goal.notificationFlag == 1) {
            view.notification_checkbox.isChecked = true
            notificationFlag = 1
        }

        val spinner: Spinner = view.findViewById(R.id.set_goals_spn)

        //Create the options for the spinner
        var options: MutableList<String> = ArrayList()
        options.add(0, "None")
        options.add(1, "10 Seconds")
        options.add(2, "Day")
        options.add(3, "Week")
        options.add(4, "Month")

        //Create array adapter to display the options list with the spinner
        val arrayAdapter = ArrayAdapter<String>(
            requireActivity().applicationContext,
            android.R.layout.simple_list_item_1,
            options
        )

        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = arrayAdapter

        //If a previous time frame was already set, update spinner to that option
        if (args.categoryChosen.goal.goalType != -1) {
            spinner.setSelection(args.categoryChosen.goal.timeFrame + 1)
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
                if (parent!!.getItemAtPosition(position).equals("None")) {
                    timeFrameFlag = -1
                    goalType = -1
                } else {

                    //Implement the goals time frame here
                    when {
                        //TODO remove this 10 second spinner after testing done
                        parent.getItemAtPosition(position) == "10 Seconds" -> {
                            timeFrameFlag = 0
                        }
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
                    goalType = 0
                }
            }
        }

        //View for checkbox for notification reminder
        val checkBox = view.findViewById(R.id.notification_checkbox) as CheckBox
        checkBox.setOnCheckedChangeListener { compoundButton, b ->
            if (b) {
                notificationFlag = 1
            } else {
                notificationFlag = 0
            }

        }

        //On Click listener for the update category button
        view.update_category_btn.setOnClickListener {
            var wordGoalCount: Int
            if (view._words_goal_count_text.text.toString().isEmpty()){
                wordGoalCount = 0
            }

            else {
                wordGoalCount = Integer.parseInt(view._words_goal_count_text.text.toString())
            }
            //Based on which spinner was chosen, detail the time for when the goal should be complete
            when (timeFrameFlag) {
                -1 -> Calendar.getInstance()
                0 -> myCalendar.add(Calendar.SECOND, 10)
                1 -> myCalendar.add(Calendar.DAY_OF_MONTH, 1)
                2 -> myCalendar.add(Calendar.DAY_OF_MONTH, 7)
                3 -> myCalendar.add(Calendar.MONTH, 1)
            }

            //Creates a goal object based on the options chosen from updating to be put into the database
            val goal: Goal = Goal(
                args.categoryChosen.goal.numWordsCompleted,
                myCalendar,
                timeFrameFlag,
                notificationFlag,
                goalType,
                wordGoalCount
            )

            val result = categoryController.updateCategory(
                args.categoryChosen.id,
                args.categoryChosen.name,
                args.categoryChosen.numWords + 1,
                args.categoryChosen.wordsList,
                args.categoryChosen.sessionNumber,
                goal
            )

            if (notificationFlag == 1 && timeFrameFlag != -1) {
                //Creates an alarmservice to run in the background
                val alarm = AlarmService(requireActivity().applicationContext, args.categoryChosen, goal)
                //Start alarm based on the option chosen in the spinner
                alarm.startAlarm()
            }

            if (result) {
                //Toast.makeText(requireContext(), "Category Has Been Updated", Toast.LENGTH_LONG).show()
                //Toast.makeText(requireContext(), "Word count is $wordGoalCount", Toast.LENGTH_LONG).show()

                findNavController().navigate(R.id.action_updateCategoryFragment_to_practice)
            }

        }

        //On Click listener for the delete category button
        view.delete_category_btn.setOnClickListener {
            val confirmation = AlertDialog.Builder(requireContext())
            confirmation.setTitle("Delete?")
            confirmation.setMessage("Are you sure you would like to delete this category?")
            confirmation.setPositiveButton("Delete") { _, _ ->
                categoryController.deleteCategory(args.categoryChosen)
                Toast.makeText(requireContext(), "Delete Success", Toast.LENGTH_SHORT).show()
                findNavController().navigate(R.id.action_updateCategoryFragment_to_practice)
            }
            confirmation.setNegativeButton("Cancel") { _, _ -> }
            confirmation.create().show()
        }

        return view
    }

    /*private fun setDefaultOption(view: TextView){
        view.setHintTextColor(Color.parseColor("#7A8089"))
        view.typeface = Typeface.DEFAULT
        view.background = ContextCompat.getDrawable(requireContext(),R.drawable.default_goal_background_color)
    }

    private fun setSelectedOption(view: TextView){
        view.setHintTextColor(Color.parseColor("#2b0475"))
        view.setTypeface(tv_goal_option_one.typeface, Typeface.BOLD)
        view.background = ContextCompat.getDrawable(requireContext(),R.drawable.selected_goal_background_color)
    }*/



}