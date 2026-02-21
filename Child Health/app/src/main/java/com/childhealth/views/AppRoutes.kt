package com.childhealth.views

import kotlinx.serialization.Serializable

@Serializable
object LaunchRoute

@Serializable
object ContentRoute

@Serializable
data class TopicRoute(
    val name: String
)

@Serializable
data class SheetRoute(
    val name: String,
    val title: String
)