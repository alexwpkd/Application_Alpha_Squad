package com.example.myapplication.View

import androidx.compose.runtime.*
import androidx.compose.material3.*
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.ui.graphics.Color

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DropdownField(
    label: String,
    items: List<String>,
    selectedItem: String,
    enabled: Boolean = true,
    onItemSelected: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded && enabled,
        onExpandedChange = { if (enabled) expanded = !expanded }
    ) {
        OutlinedTextField(
            value = selectedItem,
            onValueChange = {},
            readOnly = true,
            label = {
                Text(
                    label,
                    color = Color(0xFFEDEDED)
                )
            },
            textStyle = LocalTextStyle.current.copy(
                color = Color(0xFFEDEDED)
            ),
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
            },
            enabled = enabled,
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = Color(0xFF0E2F3A),
                unfocusedContainerColor = Color(0xFF0E2F3A),
                disabledContainerColor = Color(0xFF0E2F3A),

                focusedBorderColor = Color(0xFF6650A4),
                unfocusedBorderColor = Color(0xFF6650A4),

                focusedTextColor = Color(0xFFEDEDED),
                unfocusedTextColor = Color(0xFFEDEDED),

                focusedLabelColor = Color(0xFFEDEDED),
                unfocusedLabelColor = Color(0xFFEDEDED)
            ),
            modifier = Modifier
                .fillMaxWidth()
                .menuAnchor()
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            containerColor = Color(0xFF0E2F3A)
        ) {
            items.forEach { item ->
                DropdownMenuItem(
                    text = {
                        Text(
                            item,
                            color = Color(0xFFEDEDED)
                        )
                    },
                    onClick = {
                        onItemSelected(item)
                        expanded = false
                    }
                )
            }
        }
    }
}
