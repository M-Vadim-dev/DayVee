package com.example.dayvee.ui.screens.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dayvee.data.TaskRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

data class HomeScreenUiState (
    val username: String = "",
    val description: String = "",
    val date: LocalDate = LocalDate.now()
)

//@HiltViewModel
//class HomeScreenViewModel @Inject constructor(
//    private val repository: TaskRepository,
//) : ViewModel() {
//
//    private val _uiState = MutableStateFlow(HomeScreenUiState())
//    val uiState: StateFlow<HomeScreenUiState> = _uiState.asStateFlow()
//
//    init {
//        viewModelScope.launch {
//        }
//    }
//
//}