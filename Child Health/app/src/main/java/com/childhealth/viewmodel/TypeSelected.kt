package com.childhealth.viewmodel

enum class TypeSelected (
    val stringValue: String?
) {
    IMAGE("image"),
    TEXT("text"),
    TITLE_TEXT("titletext"),
    RED_TEXT("redtext"),
    GREEN_TEXT("greentext"),
    GRAY_TEXT("graytext"),
    LINK("link"),
    BUTTON("button")
}