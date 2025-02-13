
package com.example.roomv1.states

import com.example.roomv1.models.Factura

data class FacturaState(
    val facturasList: List<Factura> = emptyList(), // Lista de facturas
    val isLoading: Boolean = false, // Estado de carga
    val errorMessage: String? = null // Mensaje de error si ocurre
)
