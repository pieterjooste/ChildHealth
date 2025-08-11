package com.childhealth.viewmodel

import android.content.Context
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import com.childhealth.models.Sheet
import com.childhealth.models.TopicItem
import com.childhealth.extensions.getResourceIdentifier
import com.childhealth.navigateToSheet
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import java.io.IOException

class AppViewModel: ViewModel() {

    private val _topics = MutableStateFlow<List<TopicItem>>(emptyList())
    val topics: StateFlow<List<TopicItem>> = _topics

    // Other StateFlows for loading state, etc.
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    /**
     * Loads topics from the JSON asset file.
     * @param context The application context, needed to access assets.
     */
    fun loadTopics(context: Context) {
        if (_topics.value.isNotEmpty() || _isLoading.value) {
            return
        }
        _isLoading.value = true
        viewModelScope.launch {
            try {
                val jsonString = context.assets.open("childhealth.json")
                    .bufferedReader()
                    .use { it.readText() }
                val topicsList = Json.Default.decodeFromString<List<TopicItem>>(jsonString)
                _topics.value = topicsList
            } catch (e: IOException) {
                e.printStackTrace()
                _topics.value = emptyList() // Handle the error case
            } catch (e: SerializationException) {
                // Handle JSON parsing error
                e.printStackTrace()
                _topics.value = emptyList()
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun getTopicById(topicIdToFind: String): StateFlow<TopicItem?> {
        // This will map over the _topics StateFlow.
        // Whenever _topics updates, this mapping will re-evaluate.
        return _topics.map { currentTopicsList ->
            currentTopicsList.find { topic -> topic.name == topicIdToFind }
        }.stateIn(
            scope = viewModelScope, // Scope for sharing the StateFlow
            started = SharingStarted.Companion.WhileSubscribed(5000L), // Keep active for 5s after last subscriber, adjust as needed
            initialValue = null // Initial value before _topics emits or if not found immediately
        )
    }

    fun getSheetByTitle(parentTopicId: String, sheetTitleToFind: String): StateFlow<Sheet?> {
        Log.d("AppViewModel", "getSheetByTitle called for parentID: '$parentTopicId', sheetTitle: '$sheetTitleToFind'")
        return _topics.map { topicsList: List<TopicItem> ->
            val parentTopic = topicsList.find { topic -> topic.id == parentTopicId }
            var foundSheet: Sheet? = null

            if (parentTopic != null) {
                // Iterate through each section in the parent topic
                for (section in parentTopic.sections) {
                    // Iterate through each content item in the current section
                    for (contentItem in section.content) {
                        // Check if this content item has a sheet and if that sheet's title matches
                        if (contentItem.sheet != null && contentItem.sheet.title == sheetTitleToFind) {
                            foundSheet = contentItem.sheet
                            break // Found the sheet, stop searching content items
                        }
                    }
                    if (foundSheet != null) {
                        break // Found the sheet, stop searching sections
                    }
                }
            }

            Log.d("AppViewModel", "getSheetByTitle mapping for parentID '$parentTopicId', title '$sheetTitleToFind'. Parent found: ${parentTopic != null}, Sheet found: ${foundSheet != null}")
            foundSheet
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.Companion.WhileSubscribed(5000L),
            initialValue = null
        )
    }

    fun getColorFromName(colorName: String): Color {
        return when (colorName.lowercase()) {
            "clear" -> Color.Companion.Transparent
            "black" -> Color.Companion.Black
            "white" -> Color.Companion.White
            "gray" -> Color.Companion.Gray
            "red" -> Color.Companion.Red
            "green" -> Color(0xFF4CD964)
            "blue" -> Color(0xFF007AFF)
            "orange" -> Color(0xFFFFA500)
            "yellow" -> Color.Companion.Yellow
            "pink" -> Color(0xFFFFC0CB)
            "purple" -> Color.Companion.Magenta
            "primary" -> Color(0xFF6200EE)
            "secondary" -> Color(0xFF03DAC5)
            else -> Color.Companion.White
        }
    }

    @Composable
    fun TypeBuilder(
        navController: NavHostController,
        parentTopicId: String,
        //id: Int,
        content: String,
        type: String,
        linkUrl: String?,
        sheet: Sheet?,
        onLinkClick: (link: String) -> Unit
    ) {
        when (type) {
            TypeSelected.IMAGE.stringValue -> {
                Image(
                    painter = painterResource(id = content.getResourceIdentifier(LocalContext.current)),
                    contentDescription = null,
                    modifier = Modifier.Companion
                        .fillMaxWidth()
                        .padding(8.dp)
                        .clip(RoundedCornerShape(30.dp))
                )
            }

            TypeSelected.TITLE_TEXT.stringValue -> {
                Text(
                    text = content,
                    style = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.Companion.Bold)
                )
            }

            TypeSelected.RED_TEXT.stringValue -> {
                Text(
                    text = content,
                    modifier = Modifier.Companion
                        .background(
                            Color.Companion.Red,
                            shape = androidx.compose.foundation.shape.RoundedCornerShape(10.dp)
                        )
                        .padding(8.dp)
                )
            }

            TypeSelected.GREEN_TEXT.stringValue -> {
                Text(
                    text = content,
                    modifier = Modifier.Companion
                        .background(
                            Color.Companion.Green,
                            shape = androidx.compose.foundation.shape.RoundedCornerShape(10.dp)
                        )
                        .padding(8.dp)
                )
            }

            TypeSelected.GRAY_TEXT.stringValue -> {
                Text(
                    text = content,
                    modifier = Modifier.Companion
                        .background(
                            Color.Companion.Gray,
                            shape = androidx.compose.foundation.shape.RoundedCornerShape(10.dp)
                        )
                        .padding(8.dp)
                )
            }

            TypeSelected.LINK.stringValue -> {
                Text(
                    text = content,
                    color = Color.Companion.White,
                    modifier = Modifier.Companion
                        .padding(vertical = 16.dp)
                        .clickable {
                            onLinkClick(linkUrl ?: "")
                        }
                        .background(
                            Color(0xFF007AFF),
                            shape = androidx.compose.foundation.shape.RoundedCornerShape(10.dp)
                        )
                        .padding(8.dp)
                )
            }

            TypeSelected.BUTTON.stringValue -> {
                sheet?.let {
                    Button(
                        onClick = {
                            navController.navigateToSheet(parentTopicId, sheet.title)
                        },
                        modifier = Modifier.Companion
                            .padding(vertical = 4.dp)
                    ) {
                        Text(
                            text = content,
                            color = Color.Companion.White
                        )
                    }
                }
            }

            TypeSelected.TEXT.stringValue -> {
                Text(
                    text = "☉ $content",
                    modifier = Modifier.Companion
                        .padding(vertical = 4.dp)
                )
            }
        }
    }
}