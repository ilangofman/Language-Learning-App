package com.example.lyngua.models

import com.example.lyngua.models.words.Word

open class Question (
    val word: Word,
    var displayWord: String,
    var optionsList: ArrayList<String>,
    var correctAnswer: Int
    )

class MultipleChoice(
    word: Word,
    displayWord: String,
    optionsList: ArrayList<String>,
    correctAnswer: Int
) : Question(word, displayWord, optionsList, correctAnswer)

class WordMatching(
    word: Word,
    displayWord: String,
    optionsList: ArrayList<String>,
    correctAnswer: Int
) : Question(word, displayWord, optionsList, correctAnswer)

class FillInTheBlank(
    word: Word,
    displayWord: String,
    optionsList: ArrayList<String>,
    correctAnswer: Int
) : Question(word, displayWord, optionsList, correctAnswer)


