package com.childhealth.data

import android.content.Context
import com.childhealth.models.TopicItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import java.io.IOException

class TopicRepository(private val context: Context) {

    suspend fun getTopics(): Result<List<TopicItem>> {
        return withContext(Dispatchers.IO) {
            try {
                val jsonString = context.assets.open("childhealth.json")
                    .bufferedReader()
                    .use { it.readText() }
                val topicsList = Json.decodeFromString<List<TopicItem>>(jsonString)
                Result.success(topicsList)
            } catch (e: IOException) {
                Result.failure(e)
            } catch (e: SerializationException) {
                Result.failure(e)
            }
        }
    }
}