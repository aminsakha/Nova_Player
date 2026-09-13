package com.example.novaplayer.features.permission.data.repository

import com.example.novaplayer.core.permission.PermissionManager
import com.example.novaplayer.features.permission.domain.repository.PermissionRepository

class PermissionRepositoryImpl(
    private val permissionManager: PermissionManager
) : PermissionRepository {

    private var privacyAcceptedCache: Boolean = false

    override suspend fun isMusicPermissionGranted(): Boolean {
        return permissionManager.isMusicPermissionGranted()
    }

    override suspend fun isPrivacyAccepted(): Boolean {
        return privacyAcceptedCache
    }

    override suspend fun setPrivacyAccepted(accepted: Boolean) {
        privacyAcceptedCache = accepted
    }
}