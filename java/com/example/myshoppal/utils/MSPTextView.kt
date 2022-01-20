package com.example.myshoppal.utils

import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView

class MSPTextView(context : Context, attrs : AttributeSet) : AppCompatTextView(context,attrs) {

    init {
        applyFonts()
    }

    private fun applyFonts() {
        val typeFace: Typeface = Typeface.createFromAsset(context.assets, "Montserrat-Regular.ttf")
        typeface = typeFace
    }
}