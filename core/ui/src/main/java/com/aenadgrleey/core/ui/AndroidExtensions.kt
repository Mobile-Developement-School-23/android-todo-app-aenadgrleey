package com.aenadgrleey.core.ui

import android.content.Context
import android.util.TypedValue
import androidx.annotation.AttrRes
import androidx.annotation.ColorInt

@ColorInt
fun Context.resolveColorAttribute(@AttrRes attrId: Int): Int {
    val typedValue = TypedValue()
    this.theme.resolveAttribute(attrId, typedValue, true)
    return typedValue.data
}