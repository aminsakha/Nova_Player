package com.example.novaplayer.features.permission.domain.repository

interface PermissionRepository {
    suspend fun isMusicPermissionGranted(): Boolean
    suspend fun isPrivacyAccepted(): Boolean
    suspend fun setPrivacyAccepted(accepted: Boolean)
}