plugins {
    // Keep root plugin resolution limited to cross-platform plugins so desktop builds can run
    // without requiring Android Gradle Plugin artifacts.
    alias(libs.plugins.kotlin.multiplatform) apply false
    alias(libs.plugins.jetbrains.compose) apply false
    alias(libs.plugins.kotlin.compose) apply false
    alias(libs.plugins.kotlin.serialization) apply false
}
