package com.childhealth.viewmodel

import android.content.Context
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
                val topicsList = Json.decodeFromString<List<TopicItem>>(jsonString)
                _topics.value = topicsList
            } catch (_: IOException) {
                _topics.value = emptyList()
            } catch (_: SerializationException) {
                _topics.value = emptyList()
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun getTopicById(topicIdToFind: String): StateFlow<TopicItem?> {
        return _topics.map { currentTopicsList ->
            currentTopicsList.find { topic -> topic.name == topicIdToFind }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = null
        )
    }

    fun getSheetByTitle(parentTopicId: String, sheetTitleToFind: String): StateFlow<Sheet?> {
        return _topics.map { topicsList: List<TopicItem> ->
            val parentTopic = topicsList.find { topic -> topic.id == parentTopicId }
            var foundSheet: Sheet? = null

            if (parentTopic != null) {
                for (section in parentTopic.sections) {
                    for (contentItem in section.content) {
                        if (contentItem.sheet != null && contentItem.sheet.title == sheetTitleToFind) {
                            foundSheet = contentItem.sheet
                            break
                        }
                    }
                    if (foundSheet != null) {
                        break
                    }
                }
            }

            foundSheet
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = null
        )
    }

    fun getColorFromName(colorName: String): Color {
        return when (colorName.lowercase()) {
            "clear" -> Color.Transparent
            "black" -> Color.Black
            "white" -> Color.White
            "gray" -> Color.Gray
            "red" -> Color.Red
            "green" -> Color(0xFF4CD964)
            "blue" -> Color(0xFF007AFF)
            "orange" -> Color(0xFFFFA500)
            "yellow" -> Color.Yellow
            "pink" -> Color(0xFFFFC0CB)
            "purple" -> Color.Magenta
            "primary" -> Color(0xFF6200EE)
            "secondary" -> Color(0xFF03DAC5)
            else -> Color.White
        }
    }

    @Composable
    fun TypeBuilder(
        navController: NavHostController,
        parentTopicId: String,
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
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                        .clip(RoundedCornerShape(30.dp))
                )
            }

            TypeSelected.TITLE_TEXT.stringValue -> {
                Text(
                    text = content,
                    style = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.Bold)
                )
            }

            TypeSelected.RED_TEXT.stringValue -> {
                Text(
                    text = content,
                    modifier = Modifier
                        .background(
                            Color.Red,
                            shape = androidx.compose.foundation.shape.RoundedCornerShape(10.dp)
                        )
                        .padding(8.dp)
                )
            }

            TypeSelected.GREEN_TEXT.stringValue -> {
                Text(
                    text = content,
                    modifier = Modifier
                        .background(
                            Color.Green,
                            shape = androidx.compose.foundation.shape.RoundedCornerShape(10.dp)
                        )
                        .padding(8.dp)
                )
            }

            TypeSelected.GRAY_TEXT.stringValue -> {
                Text(
                    text = content,
                    modifier = Modifier
                        .background(
                            Color.Gray,
                            shape = androidx.compose.foundation.shape.RoundedCornerShape(10.dp)
                        )
                        .padding(8.dp)
                )
            }

            TypeSelected.LINK.stringValue -> {
                Text(
                    text = content,
                    color = Color.White,
                    modifier = Modifier
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
                        modifier = Modifier
                            .padding(vertical = 4.dp)
                    ) {
                        Text(
                            text = content,
                            color = Color.White
                        )
                    }
                }
            }

            TypeSelected.TEXT.stringValue -> {
                Text(
                    text = "☉ $content",
                    modifier = Modifier
                        .padding(vertical = 4.dp)
                )
            }
        }
    }
}