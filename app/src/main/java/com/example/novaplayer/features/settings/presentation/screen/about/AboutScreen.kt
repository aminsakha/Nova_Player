package com.example.novaplayer.features.settings.presentation.screen.about

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.novaplayer.R
import com.example.novaplayer.features.settings.presentation.screen.about.components.AboutActionsCard
import com.example.novaplayer.features.settings.presentation.screen.about.components.AboutHeader
import com.example.novaplayer.features.settings.presentation.screen.about.components.ContactUsDialog
import com.example.novaplayer.features.settings.presentation.screen.about.components.ProjectDescriptionCard
import com.example.novaplayer.features.settings.presentation.screen.about.components.TeamCard

@Composable
fun AboutScreen(
    onBack: () -> Unit
) {
    var showContactDialog by rememberSaveable {
        mutableStateOf(false)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                MaterialTheme.colorScheme.background
            )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = 12.dp,
                    vertical = 8.dp
                ),
            verticalAlignment = Alignment.CenterVertically
        ) {

            IconButton(
                onClick = onBack
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = stringResource(
                        R.string.back
                    )
                )
            }

            Text(
                text = stringResource(R.string.about_us),
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.SemiBold
            )

            Spacer(
                modifier = Modifier.size(48.dp)
            )
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            item {
                AboutHeader()
            }

            item {
                ProjectDescriptionCard()
            }

            item {
                TeamCard()
            }

            item {
                AboutActionsCard(
                    onPrivacyPolicyClick = {
                        // Privacy Policy should be implemented here.
                    },
                    onContactUsClick = {
                        showContactDialog = true
                    }
                )
            }
        }
    }

    if (showContactDialog) {
        ContactUsDialog(
            onDismiss = {
                showContactDialog = false
            }
        )
    }
}