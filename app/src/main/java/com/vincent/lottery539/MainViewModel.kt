package com.vincent.lottery539

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class UiState(
    val loading: Boolean = true,
    val draws: List<Draw> = emptyList(),
    val analysis: Analysis? = null,
    val message: String? = null
)

class MainViewModel : ViewModel() {
    private val repository = LotteryRepository()
    private val _state = MutableStateFlow(UiState())
    val state: StateFlow<UiState> = _state.asStateFlow()

    init { refresh() }

    fun refresh() = viewModelScope.launch(Dispatchers.IO) {
        _state.value = _state.value.copy(loading = true, message = null)
        runCatching { repository.fetchLatest() }
            .onSuccess { draws ->
                _state.value = UiState(false, draws, analyze(draws), if (draws.size < 20) "目前取得 ${draws.size} 期資料" else null)
            }
            .onFailure { error ->
                _state.value = UiState(false, message = "無法取得資料：${error.message ?: "請稍後再試"}")
            }
    }
}
