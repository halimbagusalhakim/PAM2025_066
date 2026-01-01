package com.example.bikestockproject.viewmodel


import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bikestockproject.modeldata.MerkModel
import com.example.bikestockproject.repositori.MerkRepository

import kotlinx.coroutines.launch

sealed class MerkListUiState {
    object Loading : MerkListUiState()
    data class Success(val merkList: List<MerkModel>) : MerkListUiState()
    data class Error(val message: String) : MerkListUiState()
}

sealed class DeleteMerkUiState {
    object Idle : DeleteMerkUiState()
    object Loading : DeleteMerkUiState()
    object Success : DeleteMerkUiState()
    data class Error(val message: String) : DeleteMerkUiState()
}

class MerkListViewModel(
    private val repositoryMerk: MerkRepository
) : ViewModel() {

    var merkListUiState: MerkListUiState by mutableStateOf(MerkListUiState.Loading)
        private set

    var deleteMerkUiState: DeleteMerkUiState by mutableStateOf(DeleteMerkUiState.Idle)
        private set

    fun getMerkList(token: String) {
        viewModelScope.launch {
            merkListUiState = MerkListUiState.Loading

            repositoryMerk.getAllMerk(token)
                .onSuccess { merkList ->
                    merkListUiState = MerkListUiState.Success(merkList)
                }
                .onFailure { exception ->
                    merkListUiState = MerkListUiState.Error(
                        exception.message ?: "Gagal memuat data merk"
                    )
                }
        }
    }

    fun deleteMerk(token: String, merkId: Int) {
        viewModelScope.launch {
            deleteMerkUiState = DeleteMerkUiState.Loading

            repositoryMerk.deleteMerk(token, merkId)
                .onSuccess {
                    deleteMerkUiState = DeleteMerkUiState.Success
                }
                .onFailure { exception ->
                    deleteMerkUiState = DeleteMerkUiState.Error(
                        exception.message ?: "Gagal menghapus merk"
                    )
                }
        }
    }

    fun resetDeleteState() {
        deleteMerkUiState = DeleteMerkUiState.Idle
    }
}