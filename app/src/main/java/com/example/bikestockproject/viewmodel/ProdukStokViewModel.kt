package com.example.bikestockproject.viewmodel


import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bikestockproject.repositori.ProdukRepository
import com.example.bikestockproject.uicontroller.route.DestinasiProdukStok

import kotlinx.coroutines.launch

sealed class UpdateStokUiState {
    object Idle : UpdateStokUiState()
    object Loading : UpdateStokUiState()
    object Success : UpdateStokUiState()
    data class Error(val message: String) : UpdateStokUiState()
}

data class StokFormState(
    val stokBaru: String = "",
    val isStokError: Boolean = false
)

class ProdukStokViewModel(
    savedStateHandle: SavedStateHandle,
    private val repositoryProduk: ProdukRepository
) : ViewModel() {

    var updateStokUiState: UpdateStokUiState by mutableStateOf(UpdateStokUiState.Idle)
        private set

    var formState by mutableStateOf(StokFormState())
        private set

    private val produkId: Int = checkNotNull(savedStateHandle[DestinasiProdukStok.produkIdArg])

    fun updateStokBaru(stok: String) {
        formState = formState.copy(
            stokBaru = stok,
            isStokError = false
        )
    }

    fun saveStok(token: String) {
        if (!validateInput()) return

        viewModelScope.launch {
            updateStokUiState = UpdateStokUiState.Loading

            repositoryProduk.updateStok(token, produkId, formState.stokBaru.toInt())
                .onSuccess {
                    updateStokUiState = UpdateStokUiState.Success
                }
                .onFailure { exception ->
                    updateStokUiState = UpdateStokUiState.Error(
                        exception.message ?: "Gagal memperbarui stok"
                    )
                }
        }
    }

    private fun validateInput(): Boolean {
        if (formState.stokBaru.isBlank() || formState.stokBaru.toIntOrNull() == null) {
            formState = formState.copy(isStokError = true)
            return false
        }
        return true
    }

    fun resetState() {
        updateStokUiState = UpdateStokUiState.Idle
    }
}