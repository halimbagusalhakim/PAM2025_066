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

    // Mengambil ID dari SavedStateHandle (dikirim lewat navigasi)
    private val idDariNavigasi: Int? = savedStateHandle[DestinasiMerkEdit.merkIdArg]

    /**
     * Fungsi untuk memuat data lama jika dalam mode EDIT.
     * Dipanggil di dalam LaunchedEffect pada Screen.
     */
    fun loadDataUntukEdit(token: String) {
        // Hanya dijalankan jika ada ID (mode edit) dan data namaMerk masih kosong (agar tidak re-fetch terus menerus)
        if (idDariNavigasi != null && formState.namaMerk.isEmpty()) {
            viewModelScope.launch {
                merkFormUiState = MerkFormUiState.Loading

                // Memanggil getAllMerk sesuai dengan fungsi di MerkRepository Anda
                repositoryMerk.getAllMerk(token)
                    .onSuccess { listMerk: List<MerkModel> ->
                        // Cari data merk yang ID-nya cocok dengan idDariNavigasi
                        val dataDitemukan = listMerk.find { it.merkId == idDariNavigasi }

                        if (dataDitemukan != null) {
                            formState = formState.copy(
                                merkId = dataDitemukan.merkId,
                                namaMerk = dataDitemukan.namaMerk
                            )
                            merkFormUiState = MerkFormUiState.Idle
                        } else {
                            merkFormUiState = MerkFormUiState.Error("Data merk tidak ditemukan")
                        }
                    }
                    .onFailure { e ->
                        merkFormUiState = MerkFormUiState.Error(e.message ?: "Gagal mengambil data")
                    }
            }
        }
    }

    fun updateNamaMerk(namaBaru: String) {
        formState = formState.copy(
            namaMerk = namaBaru,
            isNamaMerkError = false
        )
    }

    fun saveMerk(token: String) {
        if (!validateInput()) return

        viewModelScope.launch {
            merkFormUiState = MerkFormUiState.Loading

            val dataMerk = MerkModel(
                merkId = idDariNavigasi, // Menggunakan ID asli jika sedang edit
                namaMerk = formState.namaMerk
            )

            // Jika idDariNavigasi null maka Create, jika ada maka Update
            val hasil = if (idDariNavigasi == null) {
                repositoryMerk.createMerk(token, dataMerk)
            } else {
                repositoryMerk.updateMerk(token, dataMerk)
            }

            hasil.onSuccess {
                merkFormUiState = MerkFormUiState.Success
            }.onFailure { e ->
                merkFormUiState = MerkFormUiState.Error(e.message ?: "Gagal menyimpan data")
            }
        }
    }

    private fun validateInput(): Boolean {
        return if (formState.namaMerk.isBlank()) {
            formState = formState.copy(isNamaMerkError = true)
            false
        } else {
            true
        }
    }

    fun resetState() {
        merkFormUiState = MerkFormUiState.Idle
    }
}