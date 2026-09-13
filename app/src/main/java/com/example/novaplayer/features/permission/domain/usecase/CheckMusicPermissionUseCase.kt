package com.example.novaplayer.features.permission.domain.usecase

import com.example.novaplayer.features.permission.domain.repository.PermissionRepository

class CheckMusicPermissionUseCase(
    private val repository: PermissionRepository
) {
    suspend operator fun invoke(): Boolean {
        return repository.isMusicPermissionGranted()
    }
}