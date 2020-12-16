package com.app.lyngua.views

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.app.lyngua.R

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //Launch activity main -> this will be the first screen (the welcome screen)
        setContentView(R.layout.activity_main)

    }
}