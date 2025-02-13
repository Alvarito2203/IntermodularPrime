package com.example.roomv1.views

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
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
            modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)
        ) {
            Text(text = "Agregar Factura")
        }

        // Botones para seleccionar tipo de factura
        Row {
            Button(onClick = {
                tipoSeleccionado.value = "emitida"
                viewModel.obtenerFacturas("emitida")
            }) {
                Text("Facturas Emitidas")
            }

            Spacer(modifier = Modifier.width(16.dp))

            Button(onClick = {
                tipoSeleccionado.value = "recibida"
                viewModel.obtenerFacturas("recibida")
            }) {
                Text("Facturas Recibidas")
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
                Text(text = "Total: €${factura.total}")
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
                    }
                )
                Spacer(modifier = Modifier.width(16.dp))
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Eliminar",
                    modifier = Modifier.clickable { onDeleteClick() }
                )
            }
        }
    }
}
