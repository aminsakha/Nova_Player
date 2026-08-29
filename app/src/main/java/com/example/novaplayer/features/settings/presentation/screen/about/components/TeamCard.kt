package com.example.novaplayer.features.settings.presentation.screen.about.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.novaplayer.R

@Composable
fun TeamCard() {

    Column(
        modifier = Modifier.fillMaxWidth()
    ) {

        SectionTitle(
            title = stringResource(R.string.our_team)
        )

        Spacer(
            modifier = Modifier.height(10.dp)
        )

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(
                    alpha = 0.35f
                )
            )
        ) {

            Column(
                modifier = Modifier.padding(18.dp)
            ) {

                TeamMemberItem(
                    icon = Icons.Default.Star,
                    role = stringResource(R.string.team_lead),
                    name = stringResource(R.string.Amin_Sakha)
                )

                Spacer(
                    modifier = Modifier.height(20.dp)
                )

                Text(
                    text = stringResource(R.string.team_members),
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontWeight = FontWeight.Medium
                )

                Spacer(
                    modifier = Modifier.height(12.dp)
                )

                TeamMemberItem(
                    name = stringResource(R.string.Behnam_Mahjoob)
                )

                Spacer(
                    modifier = Modifier.height(12.dp)
                )

                TeamMemberItem(
                    name = stringResource(R.string.Abolfazl_Khalili)
                )

                Spacer(
                    modifier = Modifier.height(12.dp)
                )

                TeamMemberItem(
                    name = stringResource(R.string.Leila_Abdi)
                )

                Spacer(
                    modifier = Modifier.height(12.dp)
                )

                TeamMemberItem(
                    name = stringResource(R.string.Parsa_Ghazvinian)
                )

                Spacer(
                    modifier = Modifier.height(12.dp)
                )

                TeamMemberItem(
                    name = stringResource(R.string.Shayan_Armannia)
                )
            }
        }

        Spacer(
            modifier = Modifier.height(28.dp)
        )
    }
}