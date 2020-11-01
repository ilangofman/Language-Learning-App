package com.example.lyngua.models.categories

import android.util.Log
import com.example.lyngua.models.words.Word
import com.google.gson.GsonBuilder
import okhttp3.*
import java.io.IOException

class CategoryAPI {
    /*
    * Purpose: Use the words api to retrieve the words related to a specific category
    * Input: id - for the category database
    *        catName - the category name. The words related to this name will be retrieved
    *        repository: The rooms repository to update the category with
    * */
    fun getWordsForCategory(id: Long, catName: String, repository: CategoryRepository){
        //Build the url to get the words
        val url = "https://api.datamuse.com/words?ml=${catName}&max=100"

        val request = Request.Builder()
            .url(url)
            .build()

        val client = OkHttpClient()

        //Make the async call to retrieve the words
        client.newCall(request).enqueue(object : Callback {

            override fun onFailure(call: Call, e: IOException) {
                Log.e("Category API", "Failed to make api request")
            }

            //Since this is an async call, this will be called once the response is finished
            override fun onResponse(call: Call?, response: Response?) {
                val responseBody = response?.body()?.string()
                //Use Gson to convert from json to the Word objects
                val gson = GsonBuilder().create()
                val words = gson.fromJson(responseBody, Array<Word>::class.java)
                repository.updateCategoryWords(id.toInt(), words.toList())
            }
        })

    }

    fun getWordsForQuestion(catName: String): List<Word>? {
        //Build the url to get the words
        val url = "https://api.datamuse.com/words?ml=${catName}&max=100"

        val request = Request.Builder()
            .url(url)
            .build()

        val client = OkHttpClient()

        val response = client.newCall(request).execute()

        return if(response.isSuccessful){
            val responseBody = response?.body()?.string()
            //Use Gson to convert from json to the Word objects
            val gson = GsonBuilder().create()
            val words = gson.fromJson(responseBody, Array<Word>::class.java)
            words?.toList()
        }else{
            null
        }

    }

}
