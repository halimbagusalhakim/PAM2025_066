package com.example.bikestockproject.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bikestockproject.repositori.AuthRepository

import kotlinx.coroutines.launch

sealed class LogoutUiState {
    object Idle : LogoutUiState()
    object Loading : LogoutUiState()
    object Success : LogoutUiState()
    data class Error(val message: String) : LogoutUiState()
}

class HomeViewModel(
    private val repositoryAuth: AuthRepository
) : ViewModel() {

    var logoutUiState: LogoutUiState by mutableStateOf(LogoutUiState.Idle)
        private set

    fun logout(token: String) {
        viewModelScope.launch {
            logoutUiState = LogoutUiState.Loading

            repositoryAuth.logout(token)
                .onSuccess {
                    logoutUiState = LogoutUiState.Success
                }
                .onFailure { exception ->
                    logoutUiState = LogoutUiState.Error(
                        exception.message ?: "Logout gagal"
                    )
                }
        }
    }

    fun resetState() {
        logoutUiState = LogoutUiState.Idle
    }
}