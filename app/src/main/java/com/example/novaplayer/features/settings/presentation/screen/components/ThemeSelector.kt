package com.example.novaplayer.features.settings.presentation.screen.components

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.novaplayer.R
import com.example.novaplayer.features.settings.domain.model.ThemeMode

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

    Box(
        modifier = Modifier.fillMaxWidth()
    ) {

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(containerColor)
                .clickable {
                    expanded = !expanded
                }
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        fontWeight = FontWeight.Light,
                        text = selectedText,
                        modifier = Modifier.weight(1f),
                        style = MaterialTheme.typography.titleSmall
                    )

                    Icon(
                        imageVector = if (expanded) {
                            Icons.Default.KeyboardArrowUp
                        } else {
                            Icons.Default.KeyboardArrowDown
                        },
                        contentDescription = null
                    )
                }
                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = {
                        expanded = false
                    },
                    modifier = Modifier.fillMaxWidth(0.56f),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    ThemeMode.entries.forEach { theme ->

                        DropdownMenuItem(
                            text = {
                                Text(
                                    fontWeight = FontWeight.Light,
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
    }
}