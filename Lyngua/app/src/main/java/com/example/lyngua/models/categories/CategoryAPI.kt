package com.example.lyngua.models.categories

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.google.gson.GsonBuilder
import okhttp3.*
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL

class CategoryAPI {
    fun getWordsForCategory(){
        val url = "https://api.datamuse.com/words?ml=duck&max=100"

        val request = Request.Builder()
            .url(url)
            .build()

        val client = OkHttpClient()
        client.newCall(request).enqueue(object: Callback{
            override fun onFailure(call: Call, e: IOException) {
                Log.e("CATEGORY API", "Failed to make api request")
            }

            override fun onResponse(call: Call?, response: Response?) {
                val responseBody = response?.body()?.string()
                println(responseBody)

                val gson = GsonBuilder().create()

                val words = gson.fromJson(responseBody, Array<Word>::class.java)
            }
        })

    }

    fun convertToWords(words: List<String>){

    }
}

class Word(val word: String, val score: Int, val tags: List<String>)