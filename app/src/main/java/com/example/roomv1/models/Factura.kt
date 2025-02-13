
package com.example.roomv1.models

data class Factura(
    val id: String = "", // ID único de la factura
    val fecha: String = "", // Fecha de emisión de la factura
    val emisor: String = "", // Datos del emisor
    val receptor: String = "", // Datos del receptor
    val baseImponible: Double = 0.0, // Base imponible
    val iva: Double = 0.0, // IVA aplicado
    val total: Double = 0.0 // Total con IVA incluido
)
