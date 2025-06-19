package com.wfcorp.apiapp.ui

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.google.gson.GsonBuilder
import com.wfcorp.apiapp.ApiClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

val endpoints = listOf(
    "api/v1/catalog",
    "api/v1/catalog/help",
    "api/v1/configuration",
    "api/v1/configuration/help",
    "api/v1/datetime",
    "api/v1/datetime/help",
    "api/v1/financial/day",
    "api/v1/financial/day/help",
    "api/v1/financial/dynamic_tariff",
    "api/v1/financial/dynamic_tariff/help",
    "api/v1/financial/month",
    "api/v1/financial/month/help",
    "api/v1/financial/year",
    "api/v1/financial/year/help",
    "api/v1/indoor/temperature",
    "api/v1/indoor/temperature/day",
    "api/v1/indoor/temperature/day/help",
    "api/v1/indoor/temperature/help",
    "api/v1/indoor/temperature/hour",
    "api/v1/indoor/temperature/hour/help",
    "api/v1/indoor/temperature/minute",
    "api/v1/indoor/temperature/minute/help",
    "api/v1/indoor/temperature/month",
    "api/v1/indoor/temperature/month/help",
    "api/v1/indoor/temperature/year",
    "api/v1/indoor/temperature/year/help",
    "api/v1/p1port/telegram",
    "api/v1/p1port/telegram/help",
    "api/v1/phase",
    "api/v1/phase/help",
    "api/v1/phaseminmax/day",
    "api/v1/phaseminmax/day/help",
    "api/v1/powergas/day",
    "api/v1/powergas/day/help",
    "api/v1/powergas/hour",
    "api/v1/powergas/hour/help",
    "api/v1/powergas/minute",
    "api/v1/powergas/minute/help",
    "api/v1/powergas/month",
    "api/v1/powergas/month/help",
    "api/v1/powergas/year",
    "api/v1/powergas/year/help",
    "api/v1/powerproduction/day",
    "api/v1/powerproduction/day/help",
    "api/v1/powerproduction/hour",
    "api/v1/powerproduction/hour/help",
    "api/v1/powerproduction/minute",
    "api/v1/powerproduction/minute/help",
    "api/v1/powerproduction/month",
    "api/v1/powerproduction/month/help",
    "api/v1/powerproduction/year",
    "api/v1/powerproduction/year/help",
    "api/v1/powerproductionsolar/day/1/{db_index}",
    "api/v1/powerproductionsolar/day/help",
    "api/v1/powerproductionsolar/hour/1/{db_index}",
    "api/v1/powerproductionsolar/hour/help",
    "api/v1/powerproductionsolar/minute/1/{db_index}",
    "api/v1/powerproductionsolar/minute/help",
    "api/v1/powerproductionsolar/month/1/{db_index}",
    "api/v1/powerproductionsolar/month/help",
    "api/v1/powerproductionsolar/year/1/{db_index}",
    "api/v1/powerproductionsolar/year/help",
    "api/v1/smartmeter",
    "api/v1/smartmeter/help",
    "api/v1/status",
    "api/v1/status/help",
    "api/v1/weather",
    "api/v1/weather/day",
    "api/v1/weather/day/help",
    "api/v1/weather/help",
    "api/v1/weather/hour",
    "api/v1/weather/hour/help",
    "api/v1/weather/month",
    "api/v1/weather/month/help",
    "api/v1/weather/year",
    "api/v1/weather/year/help",
    "api/v1/wifi/ssid",
    "api/v1/wifi/ssid/help",
    "api/v2/watermeter/day",
    "api/v2/watermeter/day/help",
    "api/v2/watermeter/hour",
    "api/v2/watermeter/hour/help",
    "api/v2/watermeter/minute",
    "api/v2/watermeter/minute/help",
    "api/v2/watermeter/month",
    "api/v2/watermeter/month/help",
    "api/v2/watermeter/year",
    "api/v2/watermeter/year/help"
)

@Composable
fun ApiScreen() {
    var selectedEndpoint by remember { mutableStateOf(endpoints[0]) }
    var responseText by remember { mutableStateOf("") }
    var loading by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()

    Column(modifier = Modifier
        .fillMaxSize()
        .padding(16.dp)) {

        Text("Selecteer API-endpoint", style = MaterialTheme.typography.h6)
        Spacer(Modifier.height(8.dp))

        DropdownMenuBox(
            options = endpoints,
            selectedOption = selectedEndpoint,
            onOptionSelected = { selectedEndpoint = it }
        )

        Spacer(Modifier.height(8.dp))
        Button(
            onClick = {
                loading = true
                responseText = ""
                coroutineScope.launch(Dispatchers.IO) {
                    try {
                        val resp = ApiClient.apiService.getEndpoint(selectedEndpoint)
                        val body = resp.body()
                        responseText = GsonBuilder().setPrettyPrinting().create().toJson(body)
                    } catch (e: Exception) {
                        responseText = "Fout: ${e.message}"
                        Log.e("ApiApp", "API call failed", e)
                    } finally {
                        loading = false
                    }
                }
            }
        ) {
            Text("Ophalen")
        }

        Spacer(Modifier.height(16.dp))
        if (loading) {
            CircularProgressIndicator()
        } else {
            Text(
                text = responseText,
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(rememberScrollState())
            )
        }
    }
}

@Composable
fun DropdownMenuBox(
    options: List<String>,
    selectedOption: String,
    onOptionSelected: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    Box {
        OutlinedTextField(
            value = selectedOption,
            onValueChange = {},
            modifier = Modifier
                .fillMaxWidth()
                .clickable { expanded = true },
            label = { Text("Endpoint") },
            readOnly = true
        )
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            options.forEach { option ->
                DropdownMenuItem(onClick = {
                    onOptionSelected(option)
                    expanded = false
                }) {
                    Text(option)
                }
            }
        }
    }
}
