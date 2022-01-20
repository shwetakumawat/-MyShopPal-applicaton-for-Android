package com.example.myshoppal.utils

import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView

class MSPTextViewBold(context : Context, attrs : AttributeSet) : AppCompatTextView(context,attrs) {

    init {
        applyFonts()
    }

    private fun applyFonts() {
        val typeFace: Typeface = Typeface.createFromAsset(context.assets, "Montserrat-Bold.ttf")
        setTypeface(typeFace)
    }
}