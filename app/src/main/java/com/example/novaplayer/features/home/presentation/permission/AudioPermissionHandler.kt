package com.example.novaplayer.features.home.presentation.permission

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat

@Composable
fun AudioPermissionHandler(
    onPermissionGranted: () -> Unit,
    onPermissionDenied: () -> Unit = {}
) {
    val context = LocalContext.current

    val currentOnPermissionGranted =
        rememberUpdatedState(onPermissionGranted)

    val currentOnPermissionDenied =
        rememberUpdatedState(onPermissionDenied)

    val permission =
        if (
            Build.VERSION.SDK_INT >=
            Build.VERSION_CODES.TIRAMISU
        ) {
            Manifest.permission.READ_MEDIA_AUDIO
        } else {
            Manifest.permission.READ_EXTERNAL_STORAGE
        }

    val permissionLauncher =
        rememberLauncherForActivityResult(
            contract =
                ActivityResultContracts.RequestPermission()
        ) { isGranted ->
            if (isGranted) {
                currentOnPermissionGranted.value()
            } else {
                currentOnPermissionDenied.value()
            }
        }

    LaunchedEffect(Unit) {
        val isGranted =
            ContextCompat.checkSelfPermission(
                context,
                permission
            ) == PackageManager.PERMISSION_GRANTED

        if (isGranted) {
            currentOnPermissionGranted.value()
        } else {
            permissionLauncher.launch(permission)
        }
    }
}