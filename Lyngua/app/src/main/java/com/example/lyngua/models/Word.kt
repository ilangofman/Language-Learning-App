package com.example.lyngua.models

import kotlin.math.pow
import kotlin.math.roundToInt

class Word (
    val id: Int,
    val english: String,
    var translated: String,
    var wordsList: ArrayList<String>,
    var boxNumber: Int,
    var typeFlag: Int,
    var correctGuesses: Int,
    var totalGuesses: Int,
    var option: Int,
    var EF: Double,
    var streak: Int
){
    fun updateWord(answer: Int?){
        if(answer == option){
            boxNumber = repetitionInterval(3.0)
            streak++
            correctGuesses++
            println("Correct! Next session is ${boxNumber}")
            println("-----------------------------------------")
        }
        else{
            boxNumber = repetitionInterval( 0.0)
            if(streak >= 3)
                streak = streak - 2
            else
                streak--
            println("Incorrect! Next session is ${boxNumber}")
            println("-----------------------------------------")


        }
        if(typeFlag == 0)
            typeFlag++
        else
            typeFlag--
        totalGuesses++
    }

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
        println("English: " + english + " ||| Translated: " + translated)
        println("Other box number: " + boxNumber)
        println("Correct Guesses/Total Guesses: " + correctGuesses + "/" + totalGuesses)
    }
}