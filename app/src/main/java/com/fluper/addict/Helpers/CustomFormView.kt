package com.fluper.addict

import android.content.Context
import android.content.res.TypedArray
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import androidx.annotation.StyleableRes
import androidx.appcompat.widget.AppCompatEditText
import androidx.appcompat.widget.AppCompatTextView


class CustomFormView(context: Context, attrs: AttributeSet?) :
    LinearLayout(context, attrs) {

    @StyleableRes
    var index0 = 0

    @StyleableRes
    var index1 = 1

    var addictText: AppCompatTextView? = null
    var targetText: AppCompatEditText? = null
    var counter = 0


    private fun init(context: Context, attrs: AttributeSet) {
        View.inflate(context, R.layout.custom_form_view, this)
        val sets =
            intArrayOf(R.attr.addict_title, R.attr.target_title)
        val typedArray: TypedArray = context.obtainStyledAttributes(attrs, sets)
        val addict = typedArray.getText(index0)
        val target = typedArray.getInt(index1, 0)

        typedArray.recycle()
        initComponents()
        addictText?.text = addict
        targetText?.setText(target.toString())
        counter++
    }

    private fun initComponents() {
        addictText = findViewById(R.id.text_addict)
        targetText = findViewById(R.id.edit_text_target)
    }

    init {
        attrs?.let { init(context, it) }
    }
}