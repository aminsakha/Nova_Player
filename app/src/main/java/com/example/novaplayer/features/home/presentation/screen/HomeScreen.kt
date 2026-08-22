package com.example.novaplayer.features.home.presentation.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Preview(showSystemUi = true)
@Composable
fun HomeSc() {
    Box(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Column(Modifier.padding(8.dp)) {
            Toolbar()
            HomeTabbar()
        }
    }
}

@Composable
fun Toolbar() {
    Row(Modifier.padding(8.dp)) {
        Text(
            "Novaplayer",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )
        Spacer(Modifier.weight(1f))
        Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Icon(
                modifier = Modifier.size(32.dp),
                imageVector = Icons.Default.Search, contentDescription = ""
            )

            Icon(
                modifier = Modifier.size(32.dp),

                imageVector = Icons.Default.Menu, contentDescription = ""
            )
        }
    }
}