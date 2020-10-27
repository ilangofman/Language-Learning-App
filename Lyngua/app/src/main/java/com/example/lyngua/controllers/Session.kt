package com.example.lyngua.controllers

import com.example.lyngua.models.categories.Category
import com.example.lyngua.models.words.Word

class Session (val category: Category) {

    //Goes through the list of words using the wordIdList and generates the question
    //Inputs are
//    fun runSession(wordIdList: ArrayList<Int>){

//        var currentId = 0
//        println("Session Number: ${category.sessionNumber}")
//        for (word in category.wordsList){
//            if(wordIdList.size > currentId) {
//
//                //Only run if there are still words in the session
//                if (word.id == wordIdList[currentId]) {
//                     when (word.streak){
//                         //in  1 .. 4 -> multipleChoice(word)
//                         in  5 .. 7 -> wordMatching(word)
//                         in 8 .. 10 -> fillInTheBlank(word)
//                    }
//
//                    currentId++
//                }
//            }
//        }
//        category.sessionNumber++
//        val questions = generateSession()
//    }

    //Chooses the words that should be part of the session based on the sessionNumber and boxNumbers
    //Input parameter is the category that is being played
    //Returns an array of integers for the id of words to be used in the session
    fun generateSession():ArrayList<Question>{
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
        return generateQuestions(newSession)
    }

    private fun generateQuestions(wordIdList: ArrayList<Int>):ArrayList<Question>{

        var currentId = 0
        var option: String
        val questionsList: ArrayList<Question> = ArrayList()
        var randWord: Word
        var correctOption: Int

        //Loops through the list of words for the category
        for (word in category.wordsList){
            if(wordIdList.size > currentId) {

                //If the current word is to be in the session
                if (word.id == wordIdList[currentId]) {

                    //Choose which option is going to be the correct one
                    correctOption = (1..4).random()

                    val optionsList: ArrayList<String> = ArrayList() //List of options to be added to the question object
                    val filterList: ArrayList<Word> = ArrayList() //List of words to ensure no duplicate words are seen as options for the question

                    //Loops to add the words for the different options of the question
                    for(i in 1..4){

                        //Sets the corresponding correct option with the translated word
                        if (i == correctOption){
                            option = when(word.typeFlag){
                                0 -> word.word
                                else -> word.translated
                            }
                            optionsList.add(option)
                        }
                        //If we need to set an incorrect option with a random word from the category's word list
                        else{

                            //Choose a random word from the category's word list
                            randWord = category.wordsList.random()

                            //Will continue to choose a random word until the random word is not the word corresponding to the question and
                                    //that the word is not already an option within the list
                            while(word.id == randWord.id && filterList.contains(randWord))
                                randWord = category.wordsList.random()

                            //Add the random word to the filter list
                            filterList.add(randWord)

                            //Adds the proper translated word based on if the question is asking for english or other language translation
                            when(word.typeFlag){
                                0 -> optionsList.add(randWord.word)
                                else -> optionsList.add(randWord.translated)
                            }

                        }
                    }

                    //Creates a new question object and adds it to the list
                    //The display word depends on if the question being asked will be on english or other language translation
                    if(word.typeFlag == 0) {
                        val newQuestion = Question(word, word.translated, optionsList, correctOption)
                        questionsList.add(newQuestion)
                    }
                    else {
                        val newQuestion = Question(word, word.word, optionsList, correctOption)
                        questionsList.add(newQuestion)
                    }

                    currentId++
                }
            }
        }

        return questionsList
    }

    //Function to be integrated with multiple choice view
    //TODO change so that the correct option for the word in MC questions is done during runtime of the session
        //NOTE: This function will need to be modified
//    private fun multipleChoice(word: Word){
//        //Switching between type translation vs english
//        if (word.typeFlag % 2 == 0) {
//
//            //THIS IS TO PRINT WORDS, IS NOT NEEDED
//            //Randomize the order of the options
//            word.option = (1..4).random()
//            println("----${word.word}----")
//            var j = 0
//            for (i in 1..4) {
//                if (i == word.option) {
//                    println("${word.option}: ${word.translated}")
//                } else {
//                    //Printing of these options for words will depend on if it is from translated to english or vice versa
//                    println("$i: ${word.wordsList[j]}")
//                    j++
//                }
//            }
//
//            //User input test
//            print("\nWhat is the correct translation?: ")
//            var answer = readLine()?.toInt()
//            word.updateWord(answer)
//
//        }
//        else {
//            //THIS IS TO PRINT WORDS, IS NOT NEEDED
//            word.option = (1..4).random()
//            println("----${word.translated}----")
//            var j = 0
//
//            for (i in 1..4) {
//                if (i == word.option) {
//                    println("${word.option}: ${word.word}")
//                } else {
//                    //Printing of these options for words will depend on if it is from translated to english or vice versa
//                    println("$i: ${word.wordsList[j]}")
//                    j++
//                }
//            }
//
//            //User input test
//            print("\nWhat is the correct translation?: ")
//            var answer = readLine()?.toInt()
//            word.updateWord(answer)
//        }
//    }

    private fun wordMatching(word: Word){

    }

    private fun fillInTheBlank(word: Word){

    }
}
