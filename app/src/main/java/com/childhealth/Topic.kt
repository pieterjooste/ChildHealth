package com.childhealth

import kotlinx.serialization.Serializable


@Serializable
data class TopicItem(
    val id: String = "",
    val name: String = "",
    val sections: List<Section> = emptyList()
)
@Serializable
data class Section(
    val background: String = "",
    val content: List<Content> = emptyList(),
    val id: String = ""
)
@Serializable
data class Content(
    val content: String = "",
    val id: Int = 0,
    val linkUrl: String? = null,
    val sheet: Sheet? = null,
    val type: String = ""
)
@Serializable
data class Sheet(
    val sheetContent: List<Content> = emptyList(),
    val title: String = ""
)
