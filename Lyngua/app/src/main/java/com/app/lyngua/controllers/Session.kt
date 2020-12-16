package com.app.lyngua.controllers

import android.os.Build
import android.text.Html
import androidx.annotation.RequiresApi
import com.app.lyngua.models.*
import com.app.lyngua.models.User.User
import com.app.lyngua.models.categories.Category
import com.app.lyngua.models.words.Word

class Session(val category: Category, val user: User?) {
    val WORDS_PER_SESSION = 5
    val questionFactory: QuestionFactory = QuestionFactory()
    //Chooses the words that should be part of the session based on the sessionNumber and boxNumbers
    //Input parameter is the category that is being played
    //Returns an array of integers for the id of words to be used in the session
    @RequiresApi(Build.VERSION_CODES.N)
    fun generateSession(): ArrayList<Question> {
        val newSession = ArrayList<Int>()
        var count = 0

        while (newSession.isEmpty()) {
            for (word in category.wordsList) {

                //Add only the preset number of words for a single session
                if (category.sessionNumber == word.boxNumber && count < WORDS_PER_SESSION) {

                    newSession.add(word.id)
                    count++
                }
                //If a word has yet to be played, or if it is to be in a session but there are already
                //our a preset number words in the session, then increment the boxNumber so that the word could be in the next session
                else if (category.sessionNumber == word.boxNumber && count >= WORDS_PER_SESSION){
                    word.boxNumber++
                }

                //If a newly added word is passed, update the box number to keep up with the session number
                else if(category.sessionNumber > word.boxNumber){
                    word.boxNumber = category.sessionNumber + 1
                }

            }
            //If no words are to be in the session, then increment to the next session
            if (newSession.size == 0) {
                category.sessionNumber++
            }
        }

        return generateQuestions(newSession)
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun generateQuestions(wordIdList: ArrayList<Int>): ArrayList<Question> {

        var currentId = 0
        var option: String
        val questionsList: ArrayList<Question> = ArrayList()
        var randWord: Word
        var correctOption: Int
        var counter = 0

        //Loops through the list of words for the category
        for (word in category.wordsList) {
            // Get the translation of each word in the given category users' settings

            if (wordIdList.size > currentId) {

                //If the current word is to be in the session
                if (word.id == wordIdList[currentId]) {
                    if (user != null) {
                        val translated = Languages.translate(word.word, user.language.code).toString()
                        word.translated = Html.fromHtml(translated, Html.FROM_HTML_MODE_LEGACY).toString()
                    }
                    //Choose which option is going to be the correct one
                    correctOption = (1..4).random()

                    val optionsList: ArrayList<String> =
                        ArrayList() //List of options to be added to the question object
                    val filterList: ArrayList<Word> =
                        ArrayList() //List of words to ensure no duplicate words are seen as options for the question
                    val optionsMap = mutableMapOf<String, String>()
                    //Loops to add the words for the different options of the question
                    for (i in 1..4) {

                        //Sets the corresponding correct option with the translated word
                        if (i == correctOption) {
                            option = when (word.typeFlag) {
                                0 -> word.word
                                else -> word.translated
                            }
                            optionsList.add(option)
                            optionsMap[word.word] = word.translated
                        }
                        //If we need to set an incorrect option with a random word from the category's word list
                        else {

                            //Choose a random word from the category's word list
                            randWord = category.wordsList.random()
                            if (user != null) {
                                val translated = Languages.translate(randWord.word, user.language.code).toString()
                                randWord.translated = Html.fromHtml(translated, Html.FROM_HTML_MODE_LEGACY).toString()
                            }

                            //Will continue to choose a random word until the random word is not the word corresponding to the question and
                            //that the word is not already an option within the list
                            while (word.id == randWord.id || filterList.contains(randWord)){
                                randWord = category.wordsList.random()
                                if (user != null) {
                                    val translated =
                                        Languages.translate(randWord.word, user.language.code)
                                            .toString()
                                    randWord.translated =
                                        Html.fromHtml(translated, Html.FROM_HTML_MODE_LEGACY).toString()
                                }
                            }
                            //Add the random word to the filter list
                            filterList.add(randWord)

                            //Adds the proper translated word based on if the question is asking for english or other language translation
                            when (word.typeFlag) {
                                0 -> optionsList.add(randWord.word)
                                else -> optionsList.add(randWord.translated)
                            }
                            optionsMap[randWord.word] = randWord.translated

                        }
                    }

                    val wordMatching = mutableMapOf<String, String>()
                    wordMatching[word.word] = word.translated

                    //Creates a new question object and adds it to the list
                    //The display word depends on if the question being asked will be on english or other language translation
                    val newQuestion: Question
                    if (word.typeFlag == 0) {
                        //Depending on what streak the word is on, create the specific inherited question type
                        newQuestion = when (word.streak) {
                            in 0..3 -> questionFactory.createQuestion("multipleChoice" , word, word.translated, optionsList, correctOption, optionsMap)!!
                            in 4..6 -> questionFactory.createQuestion("wordMatching" , word, word.translated, optionsList, wordMatching, optionsMap)!!
                            else -> questionFactory.createQuestion("fillIn" , word, word.translated, optionsList, correctOption, optionsMap)!!
                        }

                    } else {
                        //Depending on what streak the word is on, create the specific inherited question type

                        newQuestion = when (word.streak) {
                            in 0..3 -> questionFactory.createQuestion("multipleChoice" , word, word.word, optionsList, correctOption, optionsMap)!!
                            in 4..6 -> questionFactory.createQuestion("wordMatching" , word, word.word, optionsList, wordMatching, optionsMap)!!
                            else -> questionFactory.createQuestion("fillIn" , word, word.word, optionsList, correctOption, optionsMap)!!
                        }
                    }

                    questionsList.add(newQuestion)
                    currentId++
                }
            }
        }

        return questionsList
    }

    companion object{
        val WORDS_PER_SESSION = 5
    }


}


