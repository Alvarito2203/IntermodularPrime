package com.example.roomv1.room

import com.example.roomv1.models.Factura
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class FacturaDatabaseDao {

    private val db = FirebaseFirestore.getInstance()
    private val facturasEmitidasCollection = db.collection("facturas_emitidas")
    private val facturasRecibidasCollection = db.collection("facturas_recibidas")

    suspend fun agregarFactura(factura: Factura) {
        try {
            if (factura.tipo == "emitida") {
                facturasEmitidasCollection.add(factura).await()
            } else {
                facturasRecibidasCollection.add(factura).await()
            }
        } catch (e: Exception) {
            throw e
        }
    }

    suspend fun obtenerFacturas(tipo: String): List<Factura> {
        return try {
            if (tipo == "emitida") {
                facturasEmitidasCollection.get().await().toObjects(Factura::class.java)
            } else {
                facturasRecibidasCollection.get().await().toObjects(Factura::class.java)
            }
        } catch (e: Exception) {
            throw e
        }
    }

    suspend fun actualizarFactura(factura: Factura) {
        try {
            val collection = if (factura.tipo == "emitida") facturasEmitidasCollection else facturasRecibidasCollection
            val querySnapshot = collection.whereEqualTo("id", factura.id).get().await()

            if (!querySnapshot.isEmpty) {
                val document = querySnapshot.documents[0]
                collection.document(document.id).set(factura).await()
            }
        } catch (e: Exception) {
            throw e
        }
    }

    suspend fun eliminarFactura(id: String, tipo: String) {
        try {
            val collection = if (tipo == "emitida") facturasEmitidasCollection else facturasRecibidasCollection
            val querySnapshot = collection.whereEqualTo("id", id).get().await()

            if (!querySnapshot.isEmpty) {
                val document = querySnapshot.documents[0]
                collection.document(document.id).delete().await()
            }
        } catch (e: Exception) {
            throw e
        }
    }
}
