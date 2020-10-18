package com.example.lyngua.views

import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.StateListDrawable
import android.util.AttributeSet
import android.view.Gravity
import com.example.lyngua.R
import com.google.cloud.translate.Language

class LanguageButton : androidx.appcompat.widget.AppCompatRadioButton {

    lateinit var language: Language

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs,
        defStyleAttr)


    constructor(context: Context, language: Language) : super(context) {
        this.language = language
        this.text = language.name
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        this.setPadding(25, 25, 25, 25)
        this.buttonDrawable = StateListDrawable()
        this.setBackgroundResource(R.drawable.radio_background)
        this.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_chevron_right_black, 0)
        this.textSize = 20F
        this.gravity = Gravity.START or Gravity.CENTER_VERTICAL
    }
}