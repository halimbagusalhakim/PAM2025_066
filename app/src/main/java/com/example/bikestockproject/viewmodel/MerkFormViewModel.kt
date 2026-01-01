package com.example.bikestockproject.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bikestockproject.modeldata.MerkModel
import com.example.bikestockproject.repositori.MerkRepository
import com.example.bikestockproject.uicontroller.route.DestinasiMerkEdit

import kotlinx.coroutines.launch

sealed class MerkFormUiState {
    object Idle : MerkFormUiState()
    object Loading : MerkFormUiState()
    object Success : MerkFormUiState()
    data class Error(val message: String) : MerkFormUiState()
}

data class MerkFormState(
    val merkId: Int? = null,
    val namaMerk: String = "",
    val isNamaMerkError: Boolean = false
)

class MerkFormViewModel(
    savedStateHandle: SavedStateHandle,
    private val repositoryMerk: MerkRepository
) : ViewModel() {

    var merkFormUiState: MerkFormUiState by mutableStateOf(MerkFormUiState.Idle)
        private set

    var formState by mutableStateOf(MerkFormState())
        private set

    private val merkId: Int? = savedStateHandle[DestinasiMerkEdit.merkIdArg]

    init {
        if (merkId != null) {
            loadMerkData()
        }
    }

    private fun loadMerkData() {
        // Implementasi load data untuk edit jika diperlukan
        // Untuk saat ini, merkId sudah tersimpan di formState
        formState = formState.copy(merkId = merkId)
    }

    fun updateNamaMerk(namaMerk: String) {
        formState = formState.copy(
            namaMerk = namaMerk,
            isNamaMerkError = false
        )
    }

    fun saveMerk(token: String) {
        if (!validateInput()) return

        viewModelScope.launch {
            merkFormUiState = MerkFormUiState.Loading

            val merk = MerkModel(
                merkId = formState.merkId,
                namaMerk = formState.namaMerk
            )

            val result = if (formState.merkId == null) {
                repositoryMerk.createMerk(token, merk)
            } else {
                repositoryMerk.updateMerk(token, merk)
            }

            result
                .onSuccess {
                    merkFormUiState = MerkFormUiState.Success
                }
                .onFailure { exception ->
                    merkFormUiState = MerkFormUiState.Error(
                        exception.message ?: "Gagal menyimpan merk"
                    )
                }
        }
    }

    private fun validateInput(): Boolean {
        if (formState.namaMerk.isBlank()) {
            formState = formState.copy(isNamaMerkError = true)
            return false
        }
        return true
    }

    fun resetState() {
        merkFormUiState = MerkFormUiState.Idle
    }
}