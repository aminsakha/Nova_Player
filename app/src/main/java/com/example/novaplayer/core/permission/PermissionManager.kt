package com.example.novaplayer.core.permission

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.content.ContextCompat

class PermissionManager(
    private val context: Context
) {

    fun getMusicPermission(): String {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            Manifest.permission.READ_MEDIA_AUDIO
        } else {
            Manifest.permission.READ_EXTERNAL_STORAGE
        }
    }

    fun isMusicPermissionGranted(): Boolean {
        return ContextCompat.checkSelfPermission(
            context,
            getMusicPermission()
        ) == PackageManager.PERMISSION_GRANTED
    }
}