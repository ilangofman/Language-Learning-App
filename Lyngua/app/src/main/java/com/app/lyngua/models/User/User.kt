package com.app.lyngua.models.User


import android.os.Parcelable
import com.google.cloud.translate.Language
import kotlinx.android.parcel.Parcelize
import java.util.*


@Parcelize
class User() : Parcelable {
    lateinit var firstName: String
    lateinit var lastName: String
    lateinit var language: Language
    var profilePicture: String? = null
    lateinit var email: String
    lateinit var dateCreated: Date

    constructor(firstName: String, lastName: String) : this() {
        this.firstName = firstName
        this.lastName = lastName
    }



}