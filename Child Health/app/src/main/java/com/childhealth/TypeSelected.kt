package com.childhealth

enum class TypeSelected (
    val stringValue: String?
) {
    IMAGE("image"),
    TEXT("text"),
    TITLE_TEXT("titletext"),
    RED_TEXT("redtext"),
    GREEN_TEXT("greentext"),
    LINK("link"),
    BUTTON("button")
}