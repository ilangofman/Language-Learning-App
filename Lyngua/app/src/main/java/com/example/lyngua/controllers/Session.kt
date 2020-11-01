package com.example.lyngua.controllers

import com.example.lyngua.models.categories.Category
import com.example.lyngua.models.words.Word

class Session(val category: Category) {

    //Chooses the words that should be part of the session based on the sessionNumber and boxNumbers
    //Input parameter is the category that is being played
    //Returns an array of integers for the id of words to be used in the session
    fun generateSession(): ArrayList<Question> {
        val newSession = ArrayList<Int>()
        var count = 0
        while (newSession.isEmpty()) {
            for (word in category.wordsList) {

                //Add only 20 words for a single session
                if (category.sessionNumber == word.boxNumber && count < 20) {

                    newSession.add(word.id)
                    count++
                }
                //If a word has yet to be played, or if it is to be in a session but there are already
                //20 words in the session, then increment the boxNumber so that the word could be in the next session
                else {
                    word.boxNumber++
                }
            }
            //If no words are to be in the session, then increment to the next session
            if (newSession.size == 0) {
                category.sessionNumber++
            }
        }
        return generateQuestions(newSession)
    }

    private fun generateQuestions(wordIdList: ArrayList<Int>): ArrayList<Question> {

        var currentId = 0
        var option: String
        val questionsList: ArrayList<Question> = ArrayList()
        var randWord: Word
        var correctOption: Int

        //Loops through the list of words for the category
        for (word in category.wordsList) {
            if (wordIdList.size > currentId) {

                //If the current word is to be in the session
                if (word.id == wordIdList[currentId]) {

                    //Choose which option is going to be the correct one
                    correctOption = (1..4).random()

                    val optionsList: ArrayList<String> =
                        ArrayList() //List of options to be added to the question object
                    val filterList: ArrayList<Word> =
                        ArrayList() //List of words to ensure no duplicate words are seen as options for the question

                    //Loops to add the words for the different options of the question
                    for (i in 1..4) {

                        //Sets the corresponding correct option with the translated word
                        if (i == correctOption) {
                            option = when (word.typeFlag) {
                                0 -> word.word
                                else -> word.translated
                            }
                            optionsList.add(option)
                        }
                        //If we need to set an incorrect option with a random word from the category's word list
                        else {

                            //Choose a random word from the category's word list
                            randWord = category.wordsList.random()

                            //Will continue to choose a random word until the random word is not the word corresponding to the question and
                            //that the word is not already an option within the list
                            while (word.id == randWord.id || filterList.contains(randWord))
                                randWord = category.wordsList.random()

                            //Add the random word to the filter list
                            filterList.add(randWord)

                            //Adds the proper translated word based on if the question is asking for english or other language translation
                            when (word.typeFlag) {
                                0 -> optionsList.add(randWord.word)
                                else -> optionsList.add(randWord.translated)
                            }

                        }
                    }

                    //Creates a new question object and adds it to the list
                    //The display word depends on if the question being asked will be on english or other language translation
                    val newQuestion: Question
                    if (word.typeFlag == 0) {

                        //Depending on what streak the word is on, create the specific inherited question type
                        when (word.streak) {
                            in 1..4 -> newQuestion =
                                MultipleChoice(word, word.translated, optionsList, correctOption)
                            in 5..7 -> newQuestion =
                                WordMatching(word, word.translated, optionsList, correctOption)
                            else -> newQuestion =
                                FillInTheBlank(word, word.translated, optionsList, correctOption)
                        }

                    } else {

                        //Depending on what streak the word is on, create the specific inherited question type
                        when (word.streak) {
                            in 1..4 -> newQuestion =
                                MultipleChoice(word, word.word, optionsList, correctOption)
                            in 5..7 -> newQuestion =
                                WordMatching(word, word.word, optionsList, correctOption)
                            else -> newQuestion =
                                FillInTheBlank(word, word.word, optionsList, correctOption)
                        }

                    }
                    questionsList.add(newQuestion)
                    currentId++
                }
            }
        }

        return questionsList
    }
}
