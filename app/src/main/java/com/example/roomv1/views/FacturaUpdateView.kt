
package com.example.roomv1.views

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.roomv1.models.Factura
import com.example.roomv1.viewmodels.FacturasViewModel

@Composable
fun FacturaUpdateView(navController: NavHostController, viewModel: FacturasViewModel, facturaId: String) {
    val factura = viewModel.state.facturasList.find { it.id == facturaId }
    val fecha = remember { mutableStateOf(factura?.fecha ?: "") }
    val emisor = remember { mutableStateOf(factura?.emisor ?: "") }
    val receptor = remember { mutableStateOf(factura?.receptor ?: "") }
    val baseImponible = remember { mutableStateOf(factura?.baseImponible?.toString() ?: "") }
    val ivaOptions = listOf("21%", "10%", "4%")
    var showIvaMenu by remember { mutableStateOf(false) }
    var selectedIva by remember { mutableStateOf(factura?.iva?.toString() ?: ivaOptions[0]) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        OutlinedTextField(
            value = fecha.value,
            onValueChange = { fecha.value = it },
            label = { Text("Fecha") },
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = emisor.value,
            onValueChange = { emisor.value = it },
            label = { Text("Emisor") },
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = receptor.value,
            onValueChange = { receptor.value = it },
            label = { Text("Receptor") },
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = baseImponible.value,
            onValueChange = { baseImponible.value = it },
            label = { Text("Base Imponible") },
            modifier = Modifier.fillMaxWidth()
        )

        Box(modifier = Modifier.fillMaxWidth()) {
            OutlinedTextField(
                value = selectedIva,
                onValueChange = {},
                label = { Text("IVA") },
                modifier = Modifier.fillMaxWidth(),
                readOnly = true,
                textStyle = LocalTextStyle.current.copy(fontSize = 14.sp),
                trailingIcon = {
                    IconButton(onClick = { showIvaMenu = true }) {
                        Icon(Icons.Default.ArrowDropDown, contentDescription = "Desplegar")
                    }
                }
            )
            DropdownMenu(
                expanded = showIvaMenu,
                onDismissRequest = { showIvaMenu = false }
            ) {
                ivaOptions.forEach { ivaOption ->
                    DropdownMenuItem(
                        text = { Text(ivaOption, fontSize = 14.sp) },
                        onClick = {
                            selectedIva = ivaOption
                            showIvaMenu = false
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                val base = baseImponible.value.toDoubleOrNull() ?: 0.0
                val iva = when (selectedIva) {
                    "21%" -> 0.21
                    "10%" -> 0.10
                    "4%" -> 0.04
                    else -> 0.0
                }
                val total = base + (base * iva)

                val updatedFactura = Factura(
                    id = factura?.id ?: "",
                    fecha = fecha.value,
                    emisor = emisor.value,
                    receptor = receptor.value,
                    baseImponible = base,
                    iva = iva,
                    total = total
                )

                viewModel.actualizarFactura(updatedFactura)
                navController.popBackStack()
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Actualizar Factura")
        }
    }
}