package com.example.lyngua.controllers

import com.example.lyngua.models.Category
import com.example.lyngua.models.Word

class Session {

    fun start(){
        val list = ArrayList<String>()
        list.add("grapes")
        list.add("apple")
        list.add("banana")
        list.add("watermelon")

        var newCategory = Category("Fruits", list)
        newCategory.generateCategory()

        println("\n\n Printing session built")
        var currentSession: ArrayList<Int>

        var flag: Int? = 1
        while (flag != 0){
            currentSession = generateSession(newCategory)
            runSession(newCategory, currentSession)
            println("\n\n Do you wish to continue? (0 = N, 1 = Y)")
            flag = readLine()?.toInt()
        }
        println("Printing results:")
        newCategory.printQuestions()
    }

    private fun runSession(category: Category, wordIdList: ArrayList<Int>){

        var currentId = 0
        println("Session Number: ${category.sessionNumber}")
        for (word in category.questionsList){
            if(wordIdList.size > currentId) {
                //Only run if there are still ask for sessions

                if (word.id == wordIdList[currentId]) {
                    if (word.streak < 5){
                        multipleChoice(word)
                    }
                    else if(word.streak >= 5 && word.streak < 8){
                        println("Word is now performing word matching")
                        wordMatching(word)
                    }
                    else if(word.streak >=8 && word.streak < 11){
                        fillInTheBlank(word)
                    }

                    currentId++
                }
            }
        }
        category.sessionNumber++
    }

    private fun generateSession(category: Category):ArrayList<Int>{
        val newSession = ArrayList<Int>()
        while (newSession.isEmpty()){
            for (word in category.questionsList) {
                if (category.sessionNumber % word.boxNumber == 0) {
                    //Randomize the options associated with the word
                    newSession.add(word.id)
                }
            }
            if (newSession.size == 0){
                category.sessionNumber++
            }
        }
        return newSession
    }

    private fun multipleChoice(word: Word){
        //Switching between type translation vs english
        if (word.typeFlag % 2 == 0) {
            //Randomize the order of the options
            word.option = (1..4).random()
            println("----${word.english}----")
            var j = 0
            for (i in 1..4) {
                if (i == word.option) {
                    println("${word.option}: ${word.translated}")
                } else {
                    //Printing of these options for words will depend on if it is from translated to english or vice versa
                    println("$i: ${word.wordsList[j]}")
                    j++
                }
            }
            //User input test
            print("\nWhat is the correct translation?: ")
            var answer = readLine()?.toInt()
            word.updateWord(answer)

        }
        else {
            word.option = (1..4).random()
            println("----${word.translated}----")
            var j = 0
            for (i in 1..4) {
                if (i == word.option) {
                    println("${word.option}: ${word.english}")
                } else {
                    //Printing of these options for words will depend on if it is from translated to english or vice versa
                    println("$i: ${word.wordsList[j]}")
                    j++
                }
            }
            //User input test
            print("\nWhat is the correct translation?: ")
            var answer = readLine()?.toInt()
            word.updateWord(answer)
        }
    }

    private fun wordMatching(word: Word){

    }

    private fun fillInTheBlank(word: Word){

    }
}