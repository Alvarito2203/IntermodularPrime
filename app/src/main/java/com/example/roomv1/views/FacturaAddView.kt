package com.example.roomv1.views

import android.app.DatePickerDialog
import android.widget.DatePicker
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.roomv1.models.Factura
import com.example.roomv1.viewmodels.FacturasViewModel
import java.util.*

@Composable
fun FacturaAddView(navController: NavHostController, viewModel: FacturasViewModel) {
    val id = remember { mutableStateOf("") }
    val fecha = remember { mutableStateOf("") }
    val emisor = remember { mutableStateOf("") }
    val receptor = remember { mutableStateOf("") }
    val baseImponible = remember { mutableStateOf("") }
    val tipoFactura = remember { mutableStateOf("emitida") }
    val ivaOptions = listOf("21%", "10%", "4%", "0%")
    var selectedIva by remember { mutableStateOf(ivaOptions[0]) }
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
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
            }
            Spacer(modifier = Modifier.width(16.dp))
            Text(text = "Agregar Nueva Factura", style = MaterialTheme.typography.headlineSmall)
        }

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(value = id.value, onValueChange = { id.value = it }, label = { Text("ID") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(value = fecha.value, onValueChange = { fecha.value = it }, label = { Text("Fecha") }, modifier = Modifier.fillMaxWidth(), readOnly = true, trailingIcon = { IconButton(onClick = { datePickerDialog.show() }) { Icon(Icons.Default.ArrowDropDown, contentDescription = "Seleccionar fecha") } })
        OutlinedTextField(value = emisor.value, onValueChange = { emisor.value = it }, label = { Text("Emisor") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(value = receptor.value, onValueChange = { receptor.value = it }, label = { Text("Receptor") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(
            value = baseImponible.value,
            onValueChange = { if (it.all { char -> char.isDigit() || char == '.' }) baseImponible.value = it },
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

        // Selección de IVA con desplegable funcional
        Box {
            OutlinedTextField(
                value = selectedIva,
                onValueChange = {},
                readOnly = true,
                label = { Text("IVA") },
                trailingIcon = {
                    IconButton(onClick = { dropdownExpanded = !dropdownExpanded }) {
                        Icon(Icons.Default.ArrowDropDown, contentDescription = "Seleccionar IVA")
                    }
                },
                modifier = Modifier.fillMaxWidth()
            )
            DropdownMenu(
                expanded = dropdownExpanded,
                onDismissRequest = { dropdownExpanded = false },
                modifier = Modifier.fillMaxWidth()
            ) {
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

        Button(
            onClick = {
                val base = baseImponible.value.toDoubleOrNull() ?: 0.0
                val iva = when (selectedIva) {
                    "21%" -> 0.21
                    "10%" -> 0.10
                    "4%" -> 0.04
                    "0%" -> 0.0
                    else -> 0.0
                }
                val total = base + (base * iva)
                val factura = Factura(
                    id = id.value,
                    fecha = fecha.value,
                    emisor = emisor.value,
                    receptor = receptor.value,
                    baseImponible = base,
                    iva = iva,
                    total = total,
                    tipo = tipoFactura.value
                )
                viewModel.agregarFactura(factura)
                navController.popBackStack()
            },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6200EE))
        ) {
            Text("Agregar Factura")
        }
    }
}