package com.aenadgrleey.tobedone.ioc

import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_NO
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_YES
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.aenadgrleey.settings.domain.SettingsRepository
import com.aenadgrleey.settings.domain.model.AppTheme
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

class ThemeController @Inject constructor(
    private val settingsRepository: SettingsRepository,
    private val lifecycleOwner: LifecycleOwner,
) {
    fun setUpThemeControl() {
        lifecycleOwner.lifecycleScope.launch {
            lifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                settingsRepository.settingFlow().collectLatest {
                    when (it.appTheme) {
                        AppTheme.Dark -> AppCompatDelegate.setDefaultNightMode(MODE_NIGHT_YES)
                        AppTheme.Light -> AppCompatDelegate.setDefaultNightMode(MODE_NIGHT_NO)
                        AppTheme.System -> AppCompatDelegate.setDefaultNightMode(MODE_NIGHT_FOLLOW_SYSTEM)
                    }
                }
            }
        }
    }
}