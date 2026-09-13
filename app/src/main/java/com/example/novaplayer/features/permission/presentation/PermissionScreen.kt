package com.example.novaplayer.features.permission.presentation

import android.Manifest
import com.example.novaplayer.R
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.novaplayer.features.permission.presentation.components.PrivacyPolicyDialog

@Composable
fun PermissionScreen(
    viewModel: PermissionViewModel,
    onNavigateToHome: () -> Unit,
    onExit: () -> Unit
) {
    val state by viewModel.state.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val colors = MaterialTheme.colorScheme

    val permission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        Manifest.permission.READ_MEDIA_AUDIO
    } else {
        Manifest.permission.READ_EXTERNAL_STORAGE
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            viewModel.onEvent(PermissionContract.Event.OnPermissionGranted)
        } else {
            viewModel.onEvent(PermissionContract.Event.OnPermissionDenied)
        }
    }

    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                PermissionContract.Effect.NavigateToHome -> onNavigateToHome()
                PermissionContract.Effect.RequestSystemPermission -> permissionLauncher.launch(permission)
                is PermissionContract.Effect.ShowMessage -> {
                    snackbarHostState.showSnackbar(effect.message)
                }
                PermissionContract.Effect.ExitApp -> onExit()
            }
        }
    }

    if (state.showPrivacyDialog) {
        PrivacyPolicyDialog(
            onDismiss = {
                viewModel.onEvent(PermissionContract.Event.OnPrivacyDialogDismissed)
            }
        )
    }

    Scaffold(
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        },
        containerColor = colors.background
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            colors.background,
                            colors.surface,
                            colors.background
                        )
                    )
                )
                .padding(innerPadding)
                .padding(horizontal = 50.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.nova_transparent),
                    contentDescription = "Music Permission",
                    modifier = Modifier.size(200.dp)
                )

                Spacer(modifier = Modifier.height(28.dp))

                Text(
                    text = "Access to Music",
                    color = colors.onBackground,
                    style = MaterialTheme.typography.headlineMedium
                )

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = "Allow permission to scan music on your device",
                    color = colors.onSurface.copy(alpha = 0.75f),
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.Center,
                    lineHeight = 22.sp
                )

                Spacer(modifier = Modifier.height(4.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Checkbox(
                        checked = state.isPrivacyAccepted,
                        onCheckedChange = {
                            viewModel.onEvent(
                                PermissionContract.Event.OnPrivacyAcceptedChanged(it)
                            )
                        },
                        colors = CheckboxDefaults.colors(
                            checkedColor = colors.primary,
                            uncheckedColor = colors.onSurface.copy(alpha = 0.7f),
                            checkmarkColor = colors.onPrimary
                        )
                    )

                    Text(
                        text = buildAnnotatedString {
                            append("I accept ")
                            withStyle(
                                style = SpanStyle(
                                    color = colors.primary,
                                    textDecoration = TextDecoration.Underline
                                )
                            ) {
                                append("Privacy Policy")
                            }
                        },
                        color = colors.onBackground,
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.clickable {
                            viewModel.onEvent(PermissionContract.Event.OnPrivacyPolicyClicked)
                        }
                    )
                }

                Spacer(modifier = Modifier.height(32.dp))

                Button(
                    onClick = {
                        viewModel.onEvent(PermissionContract.Event.OnAllowPermissionClicked)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = colors.primary,
                        contentColor = colors.onPrimary
                    )
                ) {
                    Text(
                        text = "Allow Permission",
                        style = MaterialTheme.typography.titleMedium
                    )
                }

                Spacer(modifier = Modifier.height(14.dp))

                TextButton(
                    onClick = {
                        viewModel.onEvent(PermissionContract.Event.OnNotNowClicked)
                    }
                ) {
                    Text(
                        text = "Not Now",
                        color = colors.onBackground,
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }
        }
    }
}