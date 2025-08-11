package com.childhealth.views

import kotlinx.serialization.Serializable

@Serializable
object LaunchRoute

//@Serializable
//object IntroRoute

@Serializable
object ContentRoute

@Serializable
data class TopicRoute(
    val name: String
)

@Serializable
data class SheetRoute(
    val name: String,    // ID of the parent TopicItem
    val title: String // Using the Sheet's title as its identifier for navigation
)