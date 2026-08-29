package com.example.novaplayer.features.settings.presentation.screen.about.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.novaplayer.R

@Composable
fun AboutActionsCard(
    onPrivacyPolicyClick: () -> Unit,
    onContactUsClick: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {

        SectionTitle(
            title = stringResource(R.string.more_information)
        )

        Spacer(
            modifier = Modifier.height(10.dp)
        )

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme
                    .surfaceVariant
                    .copy(alpha = 0.35f)
            )
        ) {

            Column {

                AboutActionItem(
                    icon = Icons.Default.Lock,
                    title = stringResource(
                        R.string.privacy_policy
                    ),
                    description = stringResource(
                        R.string.privacy_policy_summary
                    ),
                    onClick = onPrivacyPolicyClick
                )

                HorizontalDivider(
                    modifier = Modifier.padding(
                        horizontal = 18.dp
                    ),
                    color = MaterialTheme.colorScheme.outline.copy(
                        alpha = 0.12f
                    )
                )

                AboutActionItem(
                    icon = Icons.Default.Email,
                    title = stringResource(
                        R.string.contact_us
                    ),
                    description = stringResource(
                        R.string.contact_us_summary
                    ),
                    onClick = onContactUsClick
                )
            }
        }

        Spacer(
            modifier = Modifier.height(32.dp)
        )
    }
}

@Composable
private fun AboutActionItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    description: String,
    onClick: () -> Unit
) {
    TextButton(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                horizontal = 10.dp,
                vertical = 6.dp
            )
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            AboutIcon(
                icon = icon
            )

            Spacer(
                modifier = Modifier.width(14.dp)
            )

            Column(
                modifier = Modifier.weight(1f)
            ) {

                Text(
                    text = title,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.SemiBold
                )

                Spacer(
                    modifier = Modifier.height(3.dp)
                )

                Text(
                    text = description,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Text(
                text = "›",
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}