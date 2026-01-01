package com.example.bikestockproject.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bikestockproject.modeldata.PenjualanModel
import com.example.bikestockproject.repositori.PenjualanRepository
import com.example.bikestockproject.uicontroller.route.DestinasiPenjualanDetail

import kotlinx.coroutines.launch

sealed class PenjualanDetailUiState {
    object Loading : PenjualanDetailUiState()
    data class Success(val penjualan: PenjualanModel) : PenjualanDetailUiState()
    data class Error(val message: String) : PenjualanDetailUiState()
}

class PenjualanDetailViewModel(
    savedStateHandle: SavedStateHandle,
    private val repositoryPenjualan: PenjualanRepository
) : ViewModel() {

    var penjualanDetailUiState: PenjualanDetailUiState by mutableStateOf(PenjualanDetailUiState.Loading)
        private set

    private val penjualanId: Int = checkNotNull(savedStateHandle[DestinasiPenjualanDetail.penjualanIdArg])

    fun getPenjualanDetail(token: String) {
        viewModelScope.launch {
            penjualanDetailUiState = PenjualanDetailUiState.Loading

            repositoryPenjualan.getDetailPenjualan(token, penjualanId)
                .onSuccess { penjualan ->
                    penjualanDetailUiState = PenjualanDetailUiState.Success(penjualan)
                }
                .onFailure { exception ->
                    penjualanDetailUiState = PenjualanDetailUiState.Error(
                        exception.message ?: "Gagal memuat detail penjualan"
                    )
                }
        }
    }
}