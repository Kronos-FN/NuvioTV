package com.nuvio.tv.ui.viewmodel

import com.nuvio.tv.data.preferences.LayoutPreferencesStore
import com.nuvio.tv.domain.model.HomeLayout
import kotlinx.coroutines.flow.StateFlow

class LayoutSettingsViewModel(
    private val preferencesStore: LayoutPreferencesStore
) {
    val preferences: StateFlow<com.nuvio.tv.domain.model.LayoutPreferences> = preferencesStore.preferences

    fun selectLayout(layout: HomeLayout) {
        preferencesStore.updateHomeLayout(layout)
    }

    fun setHeroSectionEnabled(enabled: Boolean) {
        preferencesStore.updateHeroSectionEnabled(enabled)
    }

    fun setPosterLabelsEnabled(enabled: Boolean) {
        preferencesStore.updatePosterLabelsEnabled(enabled)
    }

    fun setCatalogAddonNameEnabled(enabled: Boolean) {
        preferencesStore.updateCatalogAddonNameEnabled(enabled)
    }

    fun setContinueWatchingEnabled(enabled: Boolean) {
        preferencesStore.updateContinueWatchingEnabled(enabled)
    }

    fun setDetailPageTrailerButtonEnabled(enabled: Boolean) {
        preferencesStore.updateDetailPageTrailerButtonEnabled(enabled)
    }

    fun setPreferExternalMetaAddonDetail(enabled: Boolean) {
        preferencesStore.updatePreferExternalMetaAddonDetail(enabled)
    }

    fun setFocusedPosterInfoPosition(position: String) {
        preferencesStore.updateFocusedPosterInfoPosition(position)
    }

    fun setFocusedPosterDelaySeconds(seconds: Int) {
        preferencesStore.updateFocusedPosterDelaySeconds(seconds)
    }

    fun setPosterCardWidth(width: Int) {
        preferencesStore.updatePosterCardWidth(width)
    }

    fun setPosterCardCornerRadius(radius: Int) {
        preferencesStore.updatePosterCardCornerRadius(radius)
    }

    fun resetPosterCardStyle() {
        preferencesStore.resetPosterCardStyle()
    }
}
