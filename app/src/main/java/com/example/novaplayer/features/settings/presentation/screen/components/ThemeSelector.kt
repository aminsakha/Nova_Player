package com.example.novaplayer.features.settings.presentation.screen.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
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

    val containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(
        alpha = 0.35f
    )

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = {
            expanded = !expanded
        },
        modifier = Modifier.fillMaxWidth()
    ) {

        TextField(
            value = selectedText,
            onValueChange = {},
            readOnly = true,

            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(
                    expanded = expanded
                )
            },

            modifier = Modifier
                .fillMaxWidth()
                .height(72.dp)
                .menuAnchor(),

            shape = RoundedCornerShape(16.dp),

            colors = TextFieldDefaults.colors(
                focusedContainerColor = containerColor,
                unfocusedContainerColor = containerColor,
                disabledContainerColor = containerColor,

                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent
            ),

            singleLine = true
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = {
                expanded = false
            },
            modifier = Modifier
                .fillMaxWidth(),
        ) {

            ThemeMode.entries.forEach { theme ->

                DropdownMenuItem(
                    text = {
                        Text(
                            text = when (theme) {
                                ThemeMode.SYSTEM ->
                                    stringResource(R.string.system)

                                ThemeMode.LIGHT ->
                                    stringResource(R.string.light)

                                ThemeMode.DARK ->
                                    stringResource(R.string.dark)
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