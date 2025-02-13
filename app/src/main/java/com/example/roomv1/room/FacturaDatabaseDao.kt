
package com.example.roomv1.room

import com.example.roomv1.models.Factura
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class FacturaDatabaseDao {

    private val db = FirebaseFirestore.getInstance()
    private val facturasCollection = db.collection("facturas")

    suspend fun agregarFactura(factura: Factura) {
        try {
            facturasCollection.add(factura).await()
        } catch (e: Exception) {
            throw e
        }
    }

    suspend fun obtenerFacturas(): List<Factura> {
        return try {
            facturasCollection.get().await().toObjects(Factura::class.java)
        } catch (e: Exception) {
            throw e
        }
    }

    suspend fun actualizarFactura(factura: Factura) {
        try {
            val facturaDoc = facturasCollection.whereEqualTo("id", factura.id).get().await().documents.firstOrNull()
            facturaDoc?.reference?.set(factura)?.await()
        } catch (e: Exception) {
            throw e
        }
    }

    suspend fun borrarFactura(factura: Factura) {
        try {
            val facturaDoc = facturasCollection.whereEqualTo("id", factura.id).get().await().documents.firstOrNull()
            facturaDoc?.reference?.delete()?.await()
        } catch (e: Exception) {
            throw e
        }
    }
}

