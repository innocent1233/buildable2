package com.libraryx.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier

/**
 * Single source-of-truth dropdown composable for the entire app.
 *
 * API notes (Compose BOM 2024.12.01 / material3 resolved version):
 *
 *  - [ExposedDropdownMenuAnchorType] was introduced in material3 1.3.0-alpha but the
 *    exact version resolved by the BOM may be earlier or a pre-release artefact.
 *    To guarantee compilation regardless of the exact resolved patch, we use the
 *    zero-argument [menuAnchor] overload which is present across all 1.2.x and 1.3.x
 *    releases.  The deprecation warning is suppressed explicitly so the build stays
 *    warning-free even when the toolchain surfaces it.
 *
 *  - [ExposedDropdownMenu] is an extension on [ExposedDropdownMenuBoxScope].
 *    It must be called bare (not fully-qualified) inside the [ExposedDropdownMenuBox]
 *    content lambda where the scope is available as an implicit receiver.
 *
 *  - [ExposedDropdownMenuBox] and [menuAnchor] both require
 *    @OptIn(ExperimentalMaterial3Api::class).
 *
 * Modifier split:
 *  - [modifier] is applied to the outer [ExposedDropdownMenuBox] (controls sizing / weight).
 *  - The inner [OutlinedTextField] always gets menuAnchor + fillMaxWidth so it fills the box.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppDropdown(
    label: String,
    selected: String,
    options: List<String>,
    onSelect: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = it },
        modifier = modifier
    ) {
        @Suppress("DEPRECATION")
        OutlinedTextField(
            value = selected,
            onValueChange = {},
            readOnly = true,
            label = { Text(label) },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            modifier = Modifier
                .menuAnchor()
                .fillMaxWidth()
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            options.forEach { opt ->
                DropdownMenuItem(
                    text = { Text(opt) },
                    onClick = {
                        onSelect(opt)
                        expanded = false
                    }
                )
            }
        }
    }
}
