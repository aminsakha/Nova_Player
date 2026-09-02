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
import com.example.novaplayer.features.settings.domain.model.AppLanguage

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LanguageSelector(
    selectedLanguage: AppLanguage,
    onLanguageSelected: (AppLanguage) -> Unit
) {
    var expanded by remember {
        mutableStateOf(false)
    }

    val selectedText = when (selectedLanguage) {
        AppLanguage.EN -> stringResource(R.string.english)
        AppLanguage.FA -> stringResource(R.string.persian)
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
            shape = RoundedCornerShape(16.dp)
        ) {

            AppLanguage.entries.forEach { language ->

                DropdownMenuItem(
                    text = {
                        Text(
                            text = when (language) {
                                AppLanguage.EN ->
                                    stringResource(R.string.english)

                                AppLanguage.FA ->
                                    stringResource(R.string.persian)
                            }
                        )
                    },
                    onClick = {
                        onLanguageSelected(language)
                        expanded = false
                    }
                )
            }
        }
    }
}