package com.example.roomv1.views



import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.roomv1.models.Factura

import com.example.roomv1.viewmodels.FacturasViewModel


@Composable
fun FacturaListView(navController: NavHostController, viewModel: FacturasViewModel) {
    val facturas = viewModel.state.facturasList

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Button(
            onClick = { navController.navigate(AppScreens.FacturaAddView.route) },
            modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)
        ) {
            Text(text = "Agregar Factura")
        }
        Text(text = "Lista de Facturas", modifier = Modifier.padding(8.dp))

        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(facturas) { factura ->
                FacturaItem(factura = factura, onClick = {
                    navController.navigate(AppScreens.FacturaUpdateView.route + "/${factura.id}")
                })
            }
        }
    }
}

@Composable
fun FacturaItem(factura: Factura, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { onClick() }
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = "ID: ${factura.id}")
            Text(text = "Fecha: ${factura.fecha}")
            Text(text = "Total: €${factura.total}")
        }
    }
}