package com.example.bikestockproject.viewmodel


import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bikestockproject.modeldata.ProdukModel
import com.example.bikestockproject.repositori.ProdukRepository
import com.example.bikestockproject.uicontroller.route.DestinasiProdukDetail
import kotlinx.coroutines.launch

sealed class ProdukDetailUiState {
    object Loading : ProdukDetailUiState()
    data class Success(val produk: ProdukModel) : ProdukDetailUiState()
    data class Error(val message: String) : ProdukDetailUiState()
}

class ProdukDetailViewModel(
    savedStateHandle: SavedStateHandle,
    private val repositoryProduk: ProdukRepository
) : ViewModel() {

    var produkDetailUiState: ProdukDetailUiState by mutableStateOf(ProdukDetailUiState.Loading)
        private set

    private val produkId: Int = checkNotNull(savedStateHandle[DestinasiProdukDetail.produkIdArg])

    fun getProdukDetail(token: String) {
        viewModelScope.launch {
            produkDetailUiState = ProdukDetailUiState.Loading

            repositoryProduk.getDetailProduk(token, produkId)
                .onSuccess { produk ->
                    produkDetailUiState = ProdukDetailUiState.Success(produk)
                }
                .onFailure { exception ->
                    produkDetailUiState = ProdukDetailUiState.Error(
                        exception.message ?: "Gagal memuat detail produk"
                    )
                }
        }
    }
}