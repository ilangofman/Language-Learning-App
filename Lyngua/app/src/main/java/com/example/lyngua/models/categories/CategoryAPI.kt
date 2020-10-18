package com.example.lyngua.models.categories

import android.os.Parcelable
import android.util.Log
import com.google.gson.GsonBuilder
import kotlinx.android.parcel.Parcelize
import okhttp3.OkHttpClient
import okhttp3.Request

class CategoryAPI {
    fun getWordsForCategory(): Array<Word>? {
        val url = "https://api.datamuse.com/words?ml=duck&max=100"

        val request = Request.Builder()
            .url(url)
            .build()

        val client = OkHttpClient()

//        val responseBody = client.newCall(request).execute()?.body()?.string()
        val response = client.newCall(request).execute()
        return if (response.isSuccessful) {
            val responseBody = response.body()?.string()
            val gson = GsonBuilder().create()
            val words = gson.fromJson(responseBody, Array<Word>::class.java)
            words
        } else{
            Log.e("Words API", "Could not get a successfull response for the words")
            emptyArray()
        }


        return words
//        client.newCall(request).enqueue(object: Callback{
//            override fun onFailure(call: Call, e: IOException) {
//                Log.e("CATEGORY API", "Failed to make api request")
//            }
//
//            override fun onResponse(call: Call?, response: Response?) {
//                val responseBody = response?.body()?.string()
//                println(responseBody)
//
//                val gson = GsonBuilder().create()
//
//                val words = gson.fromJson(responseBody, Array<Word>::class.java)
//            }
//        })

    }

    fun convertToWords(words: List<String>){

    }
}
@Parcelize
class Word(val word: String, val score: Int, val tags: List<String>) : Parcelable