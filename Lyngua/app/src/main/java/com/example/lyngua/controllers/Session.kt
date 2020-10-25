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
        //newCategory.generateCategory()

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
        //newCategory.printQuestions()
    }

    private fun runSession(category: Category, wordIdList: ArrayList<Int>){

        var currentId = 0
        println("Session Number: ${category.sessionNumber}")
        for (word in category.wordsList){
            if(wordIdList.size > currentId) {

                //Only run if there are still words in the session
                if (word.id == wordIdList[currentId]) {
                     when (word.streak){
                         in  1 .. 4 -> multipleChoice(word)
                         in  5 .. 7 -> wordMatching(word)
                         in 8 .. 10 -> fillInTheBlank(word)
                    }

                    currentId++
                }
            }
        }
        category.sessionNumber++
    }

    //Chooses the words that should be part of the session based on the sessionNumber and boxNumbers
    //Input parameter is the category that is being played
    //Returns an array of integers for the id of words to be used in the session
    private fun generateSession(category: Category):ArrayList<Int>{
        val newSession = ArrayList<Int>()
        var count = 0
        while (newSession.isEmpty()){
            for (word in category.wordsList) {

                //Add only 20 words for a single session
                if (category.sessionNumber == word.boxNumber && count < 20) {

                    newSession.add(word.id)
                    count++
                }
                //If a word has yet to be played, or if it is to be in a session but there are already
                    //20 words in the session, then increment the boxNumber so that the word could be in the next session
                else{
                    word.boxNumber++
                }
            }
            //If no words are to be in the session, then increment to the next session
            if (newSession.size == 0){
                category.sessionNumber++
            }
        }
        return newSession
    }

    //Function to be integrated with multiple choice view
    //TODO change so that the correct option for the word in MC questions is done during runtime of the session
        //NOTE: This function will need to be modified
    private fun multipleChoice(word: Word){
        //Switching between type translation vs english
        if (word.typeFlag % 2 == 0) {



            //THIS IS TO PRINT WORDS, IS NOT NEEDED
            //Randomize the order of the options
            word.option = (1..4).random()
            println("----${word.word}----")
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
            //THIS IS TO PRINT WORDS, IS NOT NEEDED
            word.option = (1..4).random()
            println("----${word.translated}----")
            var j = 0

            for (i in 1..4) {
                if (i == word.option) {
                    println("${word.option}: ${word.word}")
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