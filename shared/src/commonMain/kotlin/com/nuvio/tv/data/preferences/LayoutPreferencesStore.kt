package com.nuvio.tv.data.preferences

import com.nuvio.tv.domain.model.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class LayoutPreferencesStore {
    private val _preferences = MutableStateFlow(LayoutPreferences())
    val preferences: StateFlow<LayoutPreferences> = _preferences.asStateFlow()

    fun updateHomeLayout(layout: HomeLayout) {
        _preferences.value = _preferences.value.copy(homeLayout = layout)
    }

    fun updateHeroSectionEnabled(enabled: Boolean) {
        _preferences.value = _preferences.value.copy(heroSectionEnabled = enabled)
    }

    fun updatePosterLabelsEnabled(enabled: Boolean) {
        _preferences.value = _preferences.value.copy(posterLabelsEnabled = enabled)
    }

    fun updateCatalogAddonNameEnabled(enabled: Boolean) {
        _preferences.value = _preferences.value.copy(catalogAddonNameEnabled = enabled)
    }

    fun updateContinueWatchingEnabled(enabled: Boolean) {
        _preferences.value = _preferences.value.copy(continueWatchingEnabled = enabled)
    }

    fun updateDetailPageTrailerButtonEnabled(enabled: Boolean) {
        _preferences.value = _preferences.value.copy(detailPageTrailerButtonEnabled = enabled)
    }

    fun updatePreferExternalMetaAddonDetail(enabled: Boolean) {
        _preferences.value = _preferences.value.copy(preferExternalMetaAddonDetail = enabled)
    }

    fun updateFocusedPosterInfoPosition(position: String) {
        _preferences.value = _preferences.value.copy(focusedPosterInfoPosition = position)
    }

    fun updateFocusedPosterDelaySeconds(seconds: Int) {
        _preferences.value = _preferences.value.copy(focusedPosterDelaySeconds = seconds)
    }

    fun updatePosterCardWidth(width: Int) {
        _preferences.value = _preferences.value.copy(posterCardWidthDp = width)
    }

    fun updatePosterCardCornerRadius(radius: Int) {
        _preferences.value = _preferences.value.copy(posterCardCornerRadiusDp = radius)
    }

    fun resetPosterCardStyle() {
        _preferences.value = _preferences.value.copy(
            posterCardWidthDp = 126,
            posterCardCornerRadiusDp = 12
        )
    }
}
