package com.example.lyngua.models

import kotlin.random.Random

class Category(name: String, list: ArrayList<String>){
    init{
        println("Session created $list")
    }
    var id : Int? = null
    val categoryName: String? = name
    private val wordList = list
    val questionsList = ArrayList<Word>()
    var sessionNumber = 1

    fun generateCategory():ArrayList<Word>{

        var i = 1

        for (word in wordList){
            //This should be an arraylist of word objects to be able to obtain the translated word
            val guesses : ArrayList<String> = generateWordOptions()
            //Here we need to retrieve the translation of the word
            var q = Word(i, word,"translated", guesses,1, (0..1).random(),0,0, 0, 2.5, 0)
            questionsList.add(q)
            i++
        }
        return questionsList
    }

    //This function should obtain the other word options to guess
    private fun generateWordOptions():ArrayList<String>{
        val list = ArrayList<String>()
        list.add("a")
        list.add("b")
        list.add("c")
        return list
    }

    fun printQuestions(){
        for(word in questionsList){
           word.printWord()
        }
    }
}

