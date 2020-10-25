package com.example.lyngua.models.words

import kotlin.math.pow
import kotlin.math.roundToInt
import android.os.Parcelable
import android.util.Log
import kotlinx.android.parcel.Parcelize



@Parcelize
class Word (
    val score: Int,
    val tags: List<String>,
    val word: String

) : Parcelable {


    var id: Int = 0
    lateinit var translated: String
    var boxNumber: Int
    var typeFlag: Int
    var correctGuesses: Int
    var totalGuesses: Int
    var EF: Double
    var streak: Int

    companion object{
        var category_id_counter: Int = 0
    }

    init{
        id = category_id_counter
        category_id_counter += 1
        boxNumber = 1
        typeFlag = 0
        correctGuesses = 0
        totalGuesses = 0
        EF = 2.5
        streak = 0

    }

    //Initialize the words with this function because of an issue with Gson
    //It doesn't call the init function for the class so we have to call it manually
    //This is the same as the init function
    fun initWordWithGson(){
        id = category_id_counter
        category_id_counter += 1
        boxNumber = 1
        typeFlag = 0
        correctGuesses = 0
        totalGuesses = 0
        EF = 2.5
        streak = 0
    }


    fun printme(){
        Log.d("Word Created", "${this.word} initialized with id: ${this.id} and box # ${this.boxNumber}")
        println("${this.word} initialized with id: ${this.id} and box # ${this.boxNumber}")
    }

    //Updates the word's next repetition, stats, and streak for type of question received
    //NOTE: May need to break into 2 functions
    //Calls the repetition function to determine the new boxNumber for the word

//TODO make two functions for when the user gets the word correct and when incorrect

//    fun updateWord(answer: Int?){
//        if(answer == option){
//            boxNumber = repetitionInterval(4.0)
//            streak++
//            correctGuesses++
//
//        }
//        else{
//            boxNumber = repetitionInterval( 0.0)
//            if(streak >= 3)
//                streak = streak - 2
//            else
//                streak--
//
//        }
//        //Changes if the question should be asking for the english or the other language word as a question
//        if(typeFlag == 0)
//            typeFlag++
//        else
//            typeFlag--
//        totalGuesses++
//    }

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