package com.example.lyngua.models

import android.util.Log
import com.example.lyngua.models.words.Word

interface Question {
    val word: Word
    var displayWord: String
    var optionsList: ArrayList<String>
    val correctAnswer: Any

    fun getTranslatedWord() :String
}

class MultipleChoice(
    override val word: Word,
    override var displayWord: String,
    override var optionsList: ArrayList<String>,
    override val correctAnswer: Int
) : Question{
   override fun getTranslatedWord():String{
       return word.word
   }
}

class FillInTheBlank(
    override val word: Word,
    override var displayWord: String,
    override var optionsList: ArrayList<String>,
    override val correctAnswer: String
) : Question{
    override fun getTranslatedWord():String{
        return word.word
    }
}

class WordMatching(
    override val word: Word,
    override var displayWord: String,
    override var optionsList: ArrayList<String>,
    override val correctAnswer: MutableMap<String, String>,
    var optionsMap: MutableMap<String, String>

) : Question{
    override fun getTranslatedWord():String{
        return word.word
    }
}

class QuestionFactory(){
    fun createQuestion(typeQuestion: String, word: Word, displayWord: String, optionsList: ArrayList<String>, correctAnswer: Any, optionsMap: MutableMap<String, String>): Question? {
        return when (typeQuestion) {
            "multipleChoice" -> {
                MultipleChoice(word, displayWord, optionsList, correctAnswer as Int)
            }
            "fillIn" -> {
                Log.d("fill", "word: $word, displayWord: $displayWord, optionsList: $optionsList, correctAnswer: $correctAnswer")

                FillInTheBlank(word, displayWord, optionsList, optionsList[correctAnswer as Int - 1])
            }
            "wordMatching" -> {
                WordMatching(word, displayWord, optionsList, correctAnswer as MutableMap<String, String>, optionsMap)
            }
            else -> {
                null
            }
        }
    }
}
