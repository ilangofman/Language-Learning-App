package com.example.lyngua.models

import kotlin.math.pow
import kotlin.math.roundToInt
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class Word (
    val id: Int,
    val word: String,
    var translated: String,
    var wordsList: ArrayList<String>,
    var boxNumber: Int,
    var typeFlag: Int,
    var correctGuesses: Int,
    var totalGuesses: Int,
    var option: Int,
    var EF: Double,
    var streak: Int
//TODO Remove the option variable so that the session function deals with it instead
) : Parcelable {

    //Updates the word's next repetition, stats, and streak for type of question received
        //NOTE: May need to break into 2 functions
    //Calls the repetition function to determine the new boxNumber for the word
    fun updateWord(answer: Int?){
        if(answer == option){
            boxNumber = repetitionInterval(4.0)
            streak++
            correctGuesses++

        }
        else{
            boxNumber = repetitionInterval( 0.0)
            if(streak >= 3)
                streak = streak - 2
            else
                streak--

        }
        //Changes if the question should be asking for the english or the other language word as a question
        if(typeFlag == 0)
            typeFlag++
        else
            typeFlag--
        totalGuesses++
    }

    //Function that determines the next session that the word should be in based on the quality of answer
    //Returns the new boxNumber for the word
    private fun repetitionInterval(quality: Double): Int{

        //Produces a new easiness factor
        EF = EF - 0.8 + 0.28*quality-0.02*quality.pow(2)
        //If easiness factor is too small, then reset it
        if(EF < 1.5){
           EF = 1.5
        }

        return (boxNumber * EF).roundToInt()
    }

    fun printWord(){
        println("----Word----")
        println("English: " + word + " ||| Translated: " + translated)
        println("Other box number: " + boxNumber)
        println("Correct Guesses/Total Guesses: " + correctGuesses + "/" + totalGuesses)
    }
}