package com.example.bikestockproject.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bikestockproject.modeldata.PenjualanModel
import com.example.bikestockproject.modeldata.ProdukModel
import com.example.bikestockproject.repositori.PenjualanRepository
import com.example.bikestockproject.repositori.ProdukRepository
import com.example.bikestockproject.uicontroller.route.DestinasiPenjualanEdit

import kotlinx.coroutines.launch

sealed class PenjualanFormUiState {
    object Idle : PenjualanFormUiState()
    object Loading : PenjualanFormUiState()
    object Success : PenjualanFormUiState()
    data class Error(val message: String) : PenjualanFormUiState()
}

sealed class ProdukDropdownUiState {
    object Loading : ProdukDropdownUiState()
    data class Success(val produkList: List<ProdukModel>) : ProdukDropdownUiState()
    data class Error(val message: String) : ProdukDropdownUiState()
}

data class PenjualanFormState(
    val penjualanId: Int? = null,
    val produkId: Int = 0,
    val namaPembeli: String = "",
    val jumlah: String = "",
    val hargaSatuan: Int = 0,
    val totalHarga: Int = 0,
    val isProdukIdError: Boolean = false,
    val isNamaPembeliError: Boolean = false,
    val isJumlahError: Boolean = false
)

class PenjualanFormViewModel(
    savedStateHandle: SavedStateHandle,
    private val repositoryPenjualan: PenjualanRepository,
    private val repositoryProduk: ProdukRepository
) : ViewModel() {

    var penjualanFormUiState: PenjualanFormUiState by mutableStateOf(PenjualanFormUiState.Idle)
        private set

    var produkDropdownUiState: ProdukDropdownUiState by mutableStateOf(ProdukDropdownUiState.Loading)
        private set

    var formState by mutableStateOf(PenjualanFormState())
        private set

    private val penjualanId: Int? = savedStateHandle[DestinasiPenjualanEdit.penjualanIdArg]

    init {
        if (penjualanId != null) {
            formState = formState.copy(penjualanId = penjualanId)
        }
    }

    fun getProdukList(token: String) {
        viewModelScope.launch {
            produkDropdownUiState = ProdukDropdownUiState.Loading

            repositoryProduk.getAllProduk(token)
                .onSuccess { produkList ->
                    produkDropdownUiState = ProdukDropdownUiState.Success(produkList)
                }
                .onFailure { exception ->
                    produkDropdownUiState = ProdukDropdownUiState.Error(
                        exception.message ?: "Gagal memuat data produk"
                    )
                }
        }
    }

    fun loadPenjualanData(token: String) {
        if (penjualanId == null) return

        viewModelScope.launch {
            penjualanFormUiState = PenjualanFormUiState.Loading

            repositoryPenjualan.getDetailPenjualan(token, penjualanId)
                .onSuccess { penjualan ->
                    // Masukkan data lama ke dalam formState
                    formState = formState.copy(
                        penjualanId = penjualan.penjualanId,
                        produkId = penjualan.produkId,
                        namaPembeli = penjualan.namaPembeli,
                        jumlah = penjualan.jumlah.toString(),
                        // Pastikan hargaSatuan didapat dari totalHarga / jumlah jika tidak ada di model
                        hargaSatuan = if (penjualan.jumlah > 0) penjualan.totalHarga / penjualan.jumlah else 0,
                        totalHarga = penjualan.totalHarga
                    )
                    penjualanFormUiState = PenjualanFormUiState.Idle
                }
                .onFailure { exception ->
                    penjualanFormUiState = PenjualanFormUiState.Error(
                        exception.message ?: "Gagal memuat data penjualan"
                    )
                }
        }
    }

    fun updateProdukId(produkId: Int, hargaSatuan: Int) {
        formState = formState.copy(
            produkId = produkId,
            hargaSatuan = hargaSatuan,
            isProdukIdError = false
        )
        calculateTotal()
    }

    fun updateNamaPembeli(namaPembeli: String) {
        formState = formState.copy(
            namaPembeli = namaPembeli,
            isNamaPembeliError = false
        )
    }

    fun updateJumlah(jumlah: String) {
        formState = formState.copy(
            jumlah = jumlah,
            isJumlahError = false
        )
        calculateTotal()
    }

    private fun calculateTotal() {
        val jumlah = formState.jumlah.toIntOrNull() ?: 0
        val total = formState.hargaSatuan * jumlah
        formState = formState.copy(totalHarga = total)
    }

    fun savePenjualan(token: String) {
        if (!validateInput()) return

        viewModelScope.launch {
            penjualanFormUiState = PenjualanFormUiState.Loading

            val penjualan = PenjualanModel(
                penjualanId = formState.penjualanId,
                produkId = formState.produkId,
                namaPembeli = formState.namaPembeli,
                jumlah = formState.jumlah.toInt(),
                totalHarga = formState.totalHarga
            )

            val result = if (formState.penjualanId == null) {
                repositoryPenjualan.createPenjualan(token, penjualan)
            } else {
                repositoryPenjualan.updatePenjualan(token, penjualan)
            }

            result
                .onSuccess {
                    penjualanFormUiState = PenjualanFormUiState.Success
                }
                .onFailure { exception ->
                    penjualanFormUiState = PenjualanFormUiState.Error(
                        exception.message ?: "Gagal menyimpan penjualan"
                    )
                }
        }
    }

    private fun validateInput(): Boolean {
        var isValid = true

        if (formState.produkId == 0) {
            formState = formState.copy(isProdukIdError = true)
            isValid = false
        }

        if (formState.namaPembeli.isBlank()) {
            formState = formState.copy(isNamaPembeliError = true)
            isValid = false
        }

        if (formState.jumlah.isBlank() || formState.jumlah.toIntOrNull() == null || formState.jumlah.toInt() <= 0) {
            formState = formState.copy(isJumlahError = true)
            isValid = false
        }

        return isValid
    }

    fun resetState() {
        penjualanFormUiState = PenjualanFormUiState.Idle
    }
}