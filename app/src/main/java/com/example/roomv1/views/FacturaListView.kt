package com.example.roomv1.views

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.roomv1.models.Factura
import com.example.roomv1.viewmodels.FacturasViewModel
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Delete
import androidx.navigation.compose.rememberNavController

@Composable
fun FacturaListView(navController: NavHostController, viewModel: FacturasViewModel) {
    val facturas = viewModel.state.facturasList
    val tipoSeleccionado = remember { mutableStateOf("emitida") } // Estado del tipo seleccionado

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        // Botón para agregar facturas
        Button(
            onClick = { navController.navigate(AppScreens.FacturaAddView.route) },
            modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6200EE))
        ) {
            Text(text = "Agregar Factura", color = Color.White)
        }

        // Botones para seleccionar tipo de factura
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            Button(
                onClick = {
                    tipoSeleccionado.value = "emitida"
                    viewModel.obtenerFacturas("emitida")
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6200EE))
            ) {
                Text("Facturas Emitidas", color = Color.White)
            }

            Spacer(modifier = Modifier.width(16.dp))

            Button(
                onClick = {
                    tipoSeleccionado.value = "recibida"
                    viewModel.obtenerFacturas("recibida")
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6200EE))
            ) {
                Text("Facturas Recibidas", color = Color.White)
            }
        }

        Text(text = "Lista de Facturas (${tipoSeleccionado.value})", modifier = Modifier.padding(8.dp))

        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(facturas) { factura ->
                FacturaItem(
                    factura = factura,
                    onEditClick = {
                        try {
                            navController.navigate("factura_update_view/${factura.id}")
                        } catch (e: Exception) {
                            println("Error al navegar a edición: ${e.message}")
                        }
                    },
                    onDeleteClick = {
                        viewModel.eliminarFactura(factura.id, factura.tipo)
                    }
                )
            }
        }
    }
}

@Composable
fun FacturaItem(factura: Factura, onEditClick: () -> Unit, onDeleteClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Row(modifier = Modifier.padding(16.dp).fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Column {
                Text(text = "ID: ${factura.id}")
                Text(text = "Fecha: ${factura.fecha}")
                Text(text = "Total: ${factura.total}€")
            }
            Row {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = "Editar",
                    modifier = Modifier.clickable {
                        try {
                            onEditClick()
                        } catch (e: Exception) {
                            println("Error al intentar editar: ${e.message}")
                        }
                    },
                    tint = Color(0xFF6200EE)
                )
                Spacer(modifier = Modifier.width(16.dp))
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Eliminar",
                    modifier = Modifier.clickable { onDeleteClick() },
                    tint = Color(0xFF6200EE)
                )
            }
        }
    }
}