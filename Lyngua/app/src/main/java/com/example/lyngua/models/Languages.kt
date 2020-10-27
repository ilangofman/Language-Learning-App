package com.example.lyngua.models

import android.os.StrictMode
import android.util.Log
import com.google.auth.oauth2.GoogleCredentials
import com.google.cloud.translate.Language
import com.google.cloud.translate.Translate
import com.google.cloud.translate.TranslateOptions
import com.google.cloud.translate.Translation
import java.io.IOException


object Languages {

    private var translate: Translate? = null

    /*
    * Purpose: Establish the connection with the translation API
    * Input: None
    * Output: None
     */
    private fun startTranslatorService(): Translate? {
        /*
        * Guide for connecting to Translate API:
        * https://medium.com/@yeksancansu/how-to-use-google-translate-api-in-android-studio-projects-7f09cae320c7
        * */

        val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)

        try {
            val configFile = "res/raw/google_translate_api_credentials.json"

            this.javaClass.classLoader!!.getResourceAsStream(configFile).use { `is` ->
                        val myCredentials = GoogleCredentials.fromStream(`is`)
                        val translateOptions = TranslateOptions.newBuilder().setCredentials(
                            myCredentials
                        ).build()
                        translate = translateOptions.service
            }


        } catch (ioe: IOException) {
            ioe.printStackTrace()
            Log.e("Languages", "Could not establish authentication with the translate API")
        }

        return translate
    }

    /*
     * Purpose: Get a list of all the supported languages with the translate API
     * Input: None
     * Output: Optional list of languages
      */
   fun getSupportedAllLanguages(): List<Language>? {
       if(translate == null){
           translate = startTranslatorService()
       }

       return translate?.listSupportedLanguages()
   }

    fun translate(word: String, language: String): String? {
        if(translate == null){
            translate = startTranslatorService()
        }

        return translate?.translate(
            word,
            Translate.TranslateOption.targetLanguage(language),
            Translate.TranslateOption.model("base")
        )?.translatedText



    }

}