package com.example.novaplayer.features.settings.presentation.screen.about.components

import androidx.compose.foundation.clickable
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
import androidx.compose.material3.AlertDialog
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
fun ContactUsDialog(
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,

        icon = {
            Icon(
                imageVector = Icons.Default.Email,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary
            )
        },

        title = {
            Text(
                text = stringResource(R.string.contact_us),
                fontWeight = FontWeight.SemiBold
            )
        },

        text = {
            Column {

                Text(
                    text = stringResource(
                        R.string.contact_us_description
                    ),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(
                    modifier = Modifier.height(20.dp)
                )

                ContactMember(
                    name = stringResource(R.string.Amin_Sakha),
                    email = stringResource(R.string.Amin_Sakha_email)
                )

                ContactMember(
                    name = stringResource(R.string.Behnam_Mahjoob),
                    email = stringResource(R.string.Behnam_Mahjoob_email)
                )

                ContactMember(
                    name = stringResource(R.string.Abolfazl_Khalili),
                    email = stringResource(R.string.Abolfazl_Khalili_email)
                )

                ContactMember(
                    name = stringResource(R.string.Leila_Abdi),
                    email = stringResource(R.string.Leila_Abdi_email)
                )

                ContactMember(
                    name = stringResource(R.string.Parsa_Ghazvinian),
                    email = stringResource(R.string.Parsa_Ghazvinian_email)
                )

                ContactMember(
                    name = stringResource(R.string.Shayan_Armannia),
                    email = stringResource(R.string.Shayan_Armannia_email)
                )
            }
        },

        confirmButton = {
            TextButton(
                onClick = onDismiss
            ) {
                Text(
                    text = stringResource(R.string.close)
                )
            }
        },

        shape = RoundedCornerShape(28.dp)
    )
}

@Composable
private fun ContactMember(
    name: String,
    email: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                // Email action can be added later.
            }
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Icon(
            imageVector = Icons.Default.Email,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary
        )

        Spacer(
            modifier = Modifier.width(14.dp)
        )

        Column {

            Text(
                text = name,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.SemiBold
            )

            Spacer(
                modifier = Modifier.height(2.dp)
            )

            Text(
                text = email,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}