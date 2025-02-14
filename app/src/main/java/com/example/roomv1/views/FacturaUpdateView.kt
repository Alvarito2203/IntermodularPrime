package com.example.roomv1.views

import android.app.DatePickerDialog
import android.widget.DatePicker
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.roomv1.models.Factura
import com.example.roomv1.viewmodels.FacturasViewModel
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FacturaUpdateView(navController: NavHostController, viewModel: FacturasViewModel, facturaId: String) {
    val factura = viewModel.state.facturasList.find { it.id == facturaId }
    val fecha = remember { mutableStateOf(factura?.fecha ?: "") }
    val emisor = remember { mutableStateOf(factura?.emisor ?: "") }
    val receptor = remember { mutableStateOf(factura?.receptor ?: "") }
    val baseImponible = remember { mutableStateOf(factura?.baseImponible?.toString() ?: "") }
    val tipoFactura = remember { mutableStateOf(factura?.tipo ?: "emitida") }
    val ivaOptions = listOf("21%", "10%", "4%", "0%")
    var selectedIva by remember { mutableStateOf(factura?.iva?.times(100)?.toInt().toString() + "%") }
    var dropdownExpanded by remember { mutableStateOf(false) }

    val context = LocalContext.current
    val calendar = Calendar.getInstance()
    val datePickerDialog = DatePickerDialog(
        context,
        { _: DatePicker, year: Int, month: Int, dayOfMonth: Int ->
            fecha.value = "$dayOfMonth/${month + 1}/$year"
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Actualizar Factura", style = MaterialTheme.typography.headlineSmall, modifier = Modifier.padding(top = 32.dp, bottom = 16.dp))

        OutlinedTextField(value = fecha.value, onValueChange = { fecha.value = it }, label = { Text("Fecha") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(value = emisor.value, onValueChange = { emisor.value = it }, label = { Text("Emisor") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(value = receptor.value, onValueChange = { receptor.value = it }, label = { Text("Receptor") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(
            value = baseImponible.value,
            onValueChange = { baseImponible.value = it.filter { char -> char.isDigit() || char == '.' } },
            label = { Text("Base Imponible") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth()
        ) {
            RadioButton(selected = tipoFactura.value == "emitida", onClick = { tipoFactura.value = "emitida" })
            Text(text = "Emitida")
            Spacer(modifier = Modifier.width(16.dp))
            RadioButton(selected = tipoFactura.value == "recibida", onClick = { tipoFactura.value = "recibida" })
            Text(text = "Recibida")
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Selección de IVA
        ExposedDropdownMenuBox(expanded = dropdownExpanded, onExpandedChange = { dropdownExpanded = it }) {
            OutlinedTextField(
                value = selectedIva,
                onValueChange = {},
                readOnly = true,
                label = { Text("IVA") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = dropdownExpanded) },
                modifier = Modifier.fillMaxWidth()
            )
            ExposedDropdownMenu(expanded = dropdownExpanded, onDismissRequest = { dropdownExpanded = false }) {
                ivaOptions.forEach { iva ->
                    DropdownMenuItem(
                        text = { Text(iva) },
                        onClick = {
                            selectedIva = iva
                            dropdownExpanded = false
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = {
            val base = baseImponible.value.toDoubleOrNull() ?: 0.0
            val iva = when (selectedIva) {
                "21%" -> 0.21
                "10%" -> 0.10
                "4%" -> 0.04
                "0%" -> 0.0
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
                total = total,
                tipo = tipoFactura.value
            )
            viewModel.actualizarFactura(updatedFactura)
            navController.popBackStack()
        }, modifier = Modifier.fillMaxWidth()) {
            Text("Actualizar Factura")
        }
    }
}