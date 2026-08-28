package com.example.novaplayer.features.settings.presentation.screen.components

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
import androidx.compose.ui.res.stringResource
import com.example.novaplayer.R
import com.example.novaplayer.features.settings.domain.model.ThemeMode

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ThemeSelector(
    selectedTheme: ThemeMode,
    onThemeSelected: (ThemeMode) -> Unit
) {
    var expanded by remember {
        mutableStateOf(false)
    }

    val selectedText = when (selectedTheme) {
        ThemeMode.SYSTEM -> stringResource(R.string.system)
        ThemeMode.LIGHT -> stringResource(R.string.light)
        ThemeMode.DARK -> stringResource(R.string.dark)
    }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = {
            expanded = !expanded
        },
        modifier = Modifier.fillMaxWidth()
    ) {

        OutlinedTextField(
            value = selectedText,
            onValueChange = {},
            readOnly = true,
            label = {
                Text(stringResource(R.string.theme))
            },
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(
                    expanded = expanded
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .menuAnchor()
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = {
                expanded = false
            }
        ) {

            ThemeMode.entries.forEach { theme ->

                DropdownMenuItem(
                    text = {
                        Text(
                            when (theme) {
                                ThemeMode.SYSTEM -> stringResource(R.string.system)
                                ThemeMode.LIGHT -> stringResource(R.string.light)
                                ThemeMode.DARK -> stringResource(R.string.dark)
                            }
                        )
                    },
                    onClick = {
                        onThemeSelected(theme)
                        expanded = false
                    }
                )
            }
        }
    }
}