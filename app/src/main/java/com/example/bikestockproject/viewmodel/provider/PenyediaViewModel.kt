package com.example.bikestockproject.viewmodel.provider

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.bikestockproject.BikeStockApplication
import com.example.bikestockproject.viewmodel.HomeViewModel
import com.example.bikestockproject.viewmodel.LoginViewModel
import com.example.bikestockproject.viewmodel.MerkFormViewModel
import com.example.bikestockproject.viewmodel.MerkListViewModel
import com.example.bikestockproject.viewmodel.PenjualanDetailViewModel
import com.example.bikestockproject.viewmodel.PenjualanFormViewModel
import com.example.bikestockproject.viewmodel.PenjualanListViewModel
import com.example.bikestockproject.viewmodel.ProdukDetailViewModel
import com.example.bikestockproject.viewmodel.ProdukFormViewModel
import com.example.bikestockproject.viewmodel.ProdukListViewModel
import com.example.bikestockproject.viewmodel.ProdukStokViewModel

object PenyediaViewModel {

    val Factory = viewModelFactory {

        // LoginViewModel
        initializer {
            LoginViewModel(
                repositoryAuth = aplikasiBikeStock().container.repositoryAuth
            )
        }

        // HomeViewModel
        initializer {
            HomeViewModel(
                repositoryAuth = aplikasiBikeStock().container.repositoryAuth
            )
        }

        // MerkListViewModel
        initializer {
            MerkListViewModel(
                repositoryMerk = aplikasiBikeStock().container.repositoryMerk
            )
        }

        // MerkFormViewModel
        initializer {
            MerkFormViewModel(
                savedStateHandle = createSavedStateHandle(),
                repositoryMerk = aplikasiBikeStock().container.repositoryMerk
            )
        }

        // ProdukListViewModel
        initializer {
            ProdukListViewModel(
                repositoryProduk = aplikasiBikeStock().container.repositoryProduk
            )
        }

        // ProdukDetailViewModel
        initializer {
            ProdukDetailViewModel(
                savedStateHandle = createSavedStateHandle(),
                repositoryProduk = aplikasiBikeStock().container.repositoryProduk
            )
        }

        // ProdukFormViewModel
        initializer {
            ProdukFormViewModel(
                savedStateHandle = createSavedStateHandle(),
                repositoryProduk = aplikasiBikeStock().container.repositoryProduk,
                repositoryMerk = aplikasiBikeStock().container.repositoryMerk
            )
        }

        // ProdukStokViewModel
        initializer {
            ProdukStokViewModel(
                savedStateHandle = createSavedStateHandle(),
                repositoryProduk = aplikasiBikeStock().container.repositoryProduk
            )
        }

        // PenjualanListViewModel
        initializer {
            PenjualanListViewModel(
                repositoryPenjualan = aplikasiBikeStock().container.repositoryPenjualan
            )
        }

        // PenjualanDetailViewModel
        initializer {
            PenjualanDetailViewModel(
                savedStateHandle = createSavedStateHandle(),
                repositoryPenjualan = aplikasiBikeStock().container.repositoryPenjualan
            )
        }

        // PenjualanFormViewModel
        initializer {
            PenjualanFormViewModel(
                savedStateHandle = createSavedStateHandle(),
                repositoryPenjualan = aplikasiBikeStock().container.repositoryPenjualan,
                repositoryProduk = aplikasiBikeStock().container.repositoryProduk
            )
        }
    }
}

/**
 * Fungsi extension untuk mendapatkan instance BikeStockApplication
 */
fun CreationExtras.aplikasiBikeStock(): BikeStockApplication =
    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as BikeStockApplication)