package com.example.novaplayer.features.permission.domain.usecase

import com.example.novaplayer.features.permission.domain.repository.PermissionRepository

class SetPrivacyAcceptedUseCase(
    private val repository: PermissionRepository
) {
    suspend operator fun invoke(accepted: Boolean) {
        repository.setPrivacyAccepted(accepted)
    }
}