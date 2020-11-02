package com.example.lyngua.controllers

import com.example.lyngua.models.Languages
import com.example.lyngua.models.categories.CategoryAPI
import com.example.lyngua.models.words.Word
import kotlin.concurrent.thread
import kotlin.random.Random


class InteractiveController {
    val categoryAPI = CategoryAPI()

    fun makeQuestionFromWord(word: String, langCode: String): List<String>? {
        var wordsList: List<Word>? = null
        var chosenOptionsList: List<String>? = null
        thread {
             wordsList = categoryAPI.getWordsForQuestion(word)
            if (wordsList == null) {
                print("Unable to get related words")
            } else {
                var correct = Languages.translate(word, langCode).toString()
                do {
                    var options1Word = wordsList!![Random.nextInt(10, 36)].word
                    var options2Word = wordsList!![Random.nextInt(37, 67)].word
                    var options3Word = wordsList!![Random.nextInt(67, 100)].word

                    var translatedWrong1  = Languages.translate(options1Word, langCode).toString()
                    while(translatedWrong1 == options1Word){
                        options1Word = wordsList!![Random.nextInt(10, 36)].word
                        translatedWrong1  = Languages.translate(options1Word, langCode).toString()

                    }
                    var translatedWrong2  = Languages.translate(options2Word, langCode).toString()
                    while(translatedWrong2 == options2Word){
                        options2Word = wordsList!![Random.nextInt(37, 66)].word
                        translatedWrong2  = Languages.translate(options2Word, langCode).toString()

                    }
                    var translatedWrong3  = Languages.translate(options3Word, langCode).toString()
                    while(translatedWrong3 == options3Word){
                        options3Word = wordsList!![Random.nextInt(67, 100)].word
                        translatedWrong3  = Languages.translate(options3Word, langCode).toString()

                    }


                    chosenOptionsList = listOf(correct, translatedWrong1, translatedWrong2, translatedWrong3)
                }while (options1Word == options2Word
                    || options1Word == options3Word
                    || options2Word == options3Word)


            }
        }.join()



        return chosenOptionsList
    }
}