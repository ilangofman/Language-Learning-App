package com.example.lyngua.models.User

import android.content.Context
import android.content.SharedPreferences
import android.net.Uri
import android.os.Parcelable
import com.google.cloud.translate.Language
import com.google.gson.Gson
import kotlinx.android.parcel.Parcelize


@Parcelize
class User() : Parcelable {
    lateinit var firstName: String
    lateinit var lastName: String
    lateinit var language: Language
    var profilePicture: Uri? = null
    lateinit var email:String

    constructor(firstName: String, lastName: String) : this() {
        this.firstName = firstName
        this.lastName = lastName
    }



}