package com.nuvio.tv.domain.model

data class LayoutPreferences(
    val homeLayout: HomeLayout = HomeLayout.CLASSIC,
    val heroSectionEnabled: Boolean = true,
    val posterLabelsEnabled: Boolean = true,
    val catalogAddonNameEnabled: Boolean = true,
    val continueWatchingEnabled: Boolean = true,
    val detailPageTrailerButtonEnabled: Boolean = false,
    val preferExternalMetaAddonDetail: Boolean = false,
    val focusedPosterInfoPosition: String = "bottom",
    val focusedPosterDelaySeconds: Int = 3,
    val posterCardWidthDp: Int = 126,
    val posterCardCornerRadiusDp: Int = 12
)
