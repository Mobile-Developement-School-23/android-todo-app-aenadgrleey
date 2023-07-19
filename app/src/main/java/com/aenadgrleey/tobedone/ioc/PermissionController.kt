package com.aenadgrleey.tobedone.ioc

import android.Manifest
import android.os.Build
import androidx.activity.result.ActivityResultLauncher
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.aenadgrleey.settings.domain.SettingsRepository
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

class PermissionController @Inject constructor(
    private val permissionGrantLauncher: ActivityResultLauncher<String>,
    private val settingsRepository: SettingsRepository,
    private val lifecycleOwner: LifecycleOwner,
) {
    fun setUpPermissionControl() {
        lifecycleOwner.lifecycleScope.launch {
            lifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                settingsRepository.settingFlow().collectLatest {
                    if (it.logged)
                        if (!it.notificationPermissionGranted) if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) permissionGrantLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                }
            }
        }
    }
}