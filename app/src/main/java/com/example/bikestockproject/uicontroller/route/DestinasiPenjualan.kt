package com.example.bikestockproject.uicontroller.route

import com.example.bikestockproject.R

object DestinasiPenjualanList : DestinasiNavigasi {
    override val route = "list_penjualan"
    override val titleRes = R.string.list_penjualan
}

object DestinasiPenjualanEntry : DestinasiNavigasi {
    override val route = "entry_penjualan"
    override val titleRes = R.string.tambah_penjualan
}

object DestinasiPenjualanDetail : DestinasiNavigasi {
    override val route = "detail_penjualan"
    override val titleRes = R.string.detail_penjualan
    const val penjualanIdArg = "penjualanId"
    val routeWithArgs = "$route/{$penjualanIdArg}"
}

object DestinasiPenjualanEdit : DestinasiNavigasi {
    override val route = "edit_penjualan"
    override val titleRes = R.string.edit_penjualan
    const val penjualanIdArg = "penjualanId"
    val routeWithArgs = "$route/{$penjualanIdArg}"
}