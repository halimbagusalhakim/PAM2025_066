package com.example.bikestockproject.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bikestockproject.modeldata.LoginResponse
import com.example.bikestockproject.repositori.AuthRepository
import kotlinx.coroutines.launch

sealed class LoginUiState {
    object Idle : LoginUiState()
    object Loading : LoginUiState()
    data class Success(val data: LoginResponse) : LoginUiState()
    data class Error(val message: String) : LoginUiState()
}

data class LoginFormState(
    val username: String = "",
    val password: String = "",
    val isUsernameError: Boolean = false,
    val isPasswordError: Boolean = false
)

class LoginViewModel(
    private val repositoryAuth: AuthRepository
) : ViewModel() {

    var loginUiState: LoginUiState by mutableStateOf(LoginUiState.Idle)
        private set

    var formState by mutableStateOf(LoginFormState())
        private set

    fun updateUsername(username: String) {
        formState = formState.copy(
            username = username,
            isUsernameError = false
        )
    }

    fun updatePassword(password: String) {
        formState = formState.copy(
            password = password,
            isPasswordError = false
        )
    }

    fun login() {
        if (!validateInput()) return

        viewModelScope.launch {
            loginUiState = LoginUiState.Loading

            repositoryAuth.login(formState.username, formState.password)
                .onSuccess { response ->
                    loginUiState = LoginUiState.Success(response)
                }
                .onFailure { exception ->
                    loginUiState = LoginUiState.Error(
                        exception.message ?: "Login gagal"
                    )
                }
        }
    }

    private fun validateInput(): Boolean {
        var isValid = true

        if (formState.username.isBlank()) {
            formState = formState.copy(isUsernameError = true)
            isValid = false
        }

        if (formState.password.isBlank()) {
            formState = formState.copy(isPasswordError = true)
            isValid = false
        }

        return isValid
    }

    fun resetState() {
        loginUiState = LoginUiState.Idle
    }
}