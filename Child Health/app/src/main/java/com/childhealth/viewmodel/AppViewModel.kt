package com.childhealth.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.childhealth.data.TopicRepository
import com.childhealth.models.Sheet
import com.childhealth.models.TopicItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class AppViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = TopicRepository(application)

    private val _topics = MutableStateFlow<List<TopicItem>>(emptyList())
    val topics: StateFlow<List<TopicItem>> = _topics

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    /**
     * Loads topics from the repository.
     */
    fun loadTopics() {
        if (_topics.value.isNotEmpty() || _isLoading.value) {
            return
        }
        _isLoading.value = true
        viewModelScope.launch {
            repository.getTopics()
                .onSuccess {
                    _topics.value = it
                    _error.value = null
                }
                .onFailure {
                    _error.value = "Failed to load topics."
                    _topics.value = emptyList()
                }
            _isLoading.value = false
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
}