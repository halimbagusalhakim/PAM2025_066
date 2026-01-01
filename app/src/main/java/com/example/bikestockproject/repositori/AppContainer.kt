package com.example.bikestockproject.repositori

interface AppContainer {
    val repositoryAuth: AuthRepository
    val repositoryMerk: MerkRepository
    val repositoryProduk: ProdukRepository
    val repositoryPenjualan: PenjualanRepository
}

/**
 * Implementasi AppContainer
 * Menyediakan instance dari semua repository
 */
class BikeStockContainer : AppContainer {
    override val repositoryAuth: AuthRepository by lazy {
        AuthRepository()
    }

    override val repositoryMerk: MerkRepository by lazy {
        MerkRepository()
    }

    override val repositoryProduk: ProdukRepository by lazy {
        ProdukRepository()
    }

    override val repositoryPenjualan: PenjualanRepository by lazy {
        PenjualanRepository()
    }
}
