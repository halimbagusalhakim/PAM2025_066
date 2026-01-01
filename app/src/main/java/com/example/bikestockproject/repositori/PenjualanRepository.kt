package com.example.bikestockproject.repositori

import com.example.bikestockproject.apiservice.RetrofitClient
import com.example.bikestockproject.modeldata.PenjualanModel


class PenjualanRepository {

    private val api = RetrofitClient.apiService

    suspend fun getAllPenjualan(token: String): Result<List<PenjualanModel>> {
        return try {
            val response = api.getAllPenjualan("Bearer $token")
            if (response.isSuccessful && response.body()?.status == "success") {
                Result.success(response.body()?.data ?: emptyList())
            } else {
                Result.failure(Exception(response.body()?.message ?: "Gagal mengambil data penjualan"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getDetailPenjualan(token: String, penjualanId: Int): Result<PenjualanModel> {
        return try {
            val response = api.getDetailPenjualan("Bearer $token", penjualanId)
            if (response.isSuccessful && response.body()?.status == "success") {
                Result.success(response.body()?.data!!)
            } else {
                Result.failure(Exception(response.body()?.message ?: "Gagal mengambil detail penjualan"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun createPenjualan(token: String, penjualan: PenjualanModel): Result<PenjualanModel> {
        return try {
            val response = api.createPenjualan("Bearer $token", penjualan)
            if (response.isSuccessful && response.body()?.status == "success") {
                Result.success(response.body()?.data!!)
            } else {
                Result.failure(Exception(response.body()?.message ?: "Gagal menambah penjualan"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun updatePenjualan(token: String, penjualan: PenjualanModel): Result<PenjualanModel> {
        return try {
            val response = api.updatePenjualan("Bearer $token", penjualan)
            if (response.isSuccessful && response.body()?.status == "success") {
                Result.success(response.body()?.data!!)
            } else {
                Result.failure(Exception(response.body()?.message ?: "Gagal mengubah penjualan"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun deletePenjualan(token: String, penjualanId: Int): Result<String> {
        return try {
            val response = api.deletePenjualan("Bearer $token", penjualanId)
            if (response.isSuccessful && response.body()?.status == "success") {
                Result.success(response.body()?.message ?: "Berhasil menghapus penjualan")
            } else {
                Result.failure(Exception(response.body()?.message ?: "Gagal menghapus penjualan"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}