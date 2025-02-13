package com.example.roomv1.views


import android.app.DatePickerDialog
import android.widget.DatePicker
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
    val ivaOptions = listOf("21%", "10%", "4%")
    var showIvaMenu by remember { mutableStateOf(false) }
    var selectedIva by remember { mutableStateOf(ivaOptions[0]) }

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
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Campos para ingresar datos de la factura
        OutlinedTextField(
            value = id.value,
            onValueChange = { id.value = it },
            label = { Text("ID") },
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = fecha.value,
            onValueChange = { fecha.value = it },
            label = { Text("Fecha") },
            modifier = Modifier.fillMaxWidth(),
            readOnly = true,
            trailingIcon = {
                IconButton(onClick = { datePickerDialog.show() }) {
                    Icon(Icons.Default.ArrowDropDown, contentDescription = "Seleccionar fecha")
                }
            }
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

                val factura = Factura(
                    id = id.value,
                    fecha = fecha.value,
                    emisor = emisor.value,
                    receptor = receptor.value,
                    baseImponible = base,
                    iva = iva,
                    total = total
                )

                viewModel.agregarFactura(factura)
                navController.popBackStack()
                viewModel.obtenerFacturas()
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Agregar Factura")
        }
    }
}