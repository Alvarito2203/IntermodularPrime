package com.example.roomv1.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.roomv1.models.Factura
import com.example.roomv1.room.FacturaDatabaseDao
import com.example.roomv1.states.FacturaState
import kotlinx.coroutines.launch

class FacturasViewModel(private val dao: FacturaDatabaseDao) : ViewModel() {

    var state by mutableStateOf(FacturaState())
        private set

    init {
        obtenerFacturas()
    }

    fun obtenerFacturas() {
        viewModelScope.launch {
            state = state.copy(isLoading = true)
            try {
                val facturas = dao.obtenerFacturas()
                state = state.copy(facturasList = facturas, isLoading = false)
            } catch (e: Exception) {
                state = state.copy(errorMessage = e.message, isLoading = false)
            }
        }
    }

    fun agregarFactura(factura: Factura) {
        viewModelScope.launch {
            try {
                dao.agregarFactura(factura)
                obtenerFacturas()
            } catch (e: Exception) {
                state = state.copy(errorMessage = e.message)
            }
        }
    }

    fun actualizarFactura(factura: Factura) {
        viewModelScope.launch {
            try {
                dao.actualizarFactura(factura)
                obtenerFacturas()
            } catch (e: Exception) {
                state = state.copy(errorMessage = e.message)
            }
        }
    }

    fun borrarFactura(factura: Factura) {
        viewModelScope.launch {
            try {
                dao.borrarFactura(factura)
                obtenerFacturas()
            } catch (e: Exception) {
                state = state.copy(errorMessage = e.message)
            }
        }
    }
}
