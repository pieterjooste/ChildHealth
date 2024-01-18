package com.childhealth.extensions

import android.content.Context

fun String.getResourceIdentifier(context: Context): Int {
    return context.resources.getIdentifier(
        this,
        "drawable",
        context.packageName
    )
}