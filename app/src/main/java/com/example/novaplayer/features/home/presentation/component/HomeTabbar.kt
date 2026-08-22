package com.example.novaplayer.features.home.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.novaplayer.features.home.data.HomeTabs

@Composable
fun HomeTabBar(
    selectedTab: HomeTabs,
    onTabSelected: (HomeTabs) -> Unit
) {
    val tabs = HomeTabs.entries

    TabRow(
        selectedTabIndex = selectedTab.ordinal,
        divider = {},
        indicator = { tabPositions ->

            Box(
                modifier = Modifier
                    .tabIndicatorOffset(tabPositions[selectedTab.ordinal])
                    .padding(horizontal = 16.dp)
                    .height(2.dp)
                    .background(
                        color = MaterialTheme.colorScheme.primary,
                        shape = RoundedCornerShape(50)
                    )
            )
        }
    ) {
        tabs.forEach { tab ->

            val isSelected = selectedTab == tab

            Box(
                modifier = Modifier
                    .clickable {
                        onTabSelected(tab)
                    }
                    .padding(8.dp)
            ) {
                Text(
                    textAlign = TextAlign.Center,
                    text = stringResource(tab.titleRes),
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.Bold,
                    color = if (isSelected) {
                        MaterialTheme.colorScheme.primary
                    } else {
                        Color.Black.copy(alpha = 0.5f)
                    },
                    modifier = Modifier
                        .padding(8.dp)
                        .fillMaxWidth()

                )
            }
        }
    }
}