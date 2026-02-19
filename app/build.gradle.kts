plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.hilt)
    alias(libs.plugins.ksp)
    alias(libs.plugins.kotlin.serialization)
}

import java.util.Properties

val localProperties = Properties().apply {
    val localPropertiesFile = rootProject.file("local.properties")
    if (localPropertiesFile.exists()) {
        load(localPropertiesFile.inputStream())
    }
}

val devProperties = Properties().apply {
    val devPropertiesFile = rootProject.file("local.dev.properties")
    if (devPropertiesFile.exists()) {
        load(devPropertiesFile.inputStream())
    }
}

android {
    namespace = "com.nuvio.tv"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.nuvio.tv"
        minSdk = 26
        targetSdk = 36
        versionCode = 16
        versionName = "0.3.7-beta"

        buildConfigField("String", "PARENTAL_GUIDE_API_URL", "\"${localProperties.getProperty("PARENTAL_GUIDE_API_URL", "")}\"")
        buildConfigField("String", "INTRODB_API_URL", "\"${localProperties.getProperty("INTRODB_API_URL", "")}\"")
        buildConfigField("String", "TRAILER_API_URL", "\"${localProperties.getProperty("TRAILER_API_URL", "")}\"")
        buildConfigField("String", "IMDB_RATINGS_API_BASE_URL", "\"${localProperties.getProperty("IMDB_RATINGS_API_BASE_URL", "")}\"")
        buildConfigField("String", "TRAKT_CLIENT_ID", "\"${localProperties.getProperty("TRAKT_CLIENT_ID", "")}\"")
        buildConfigField("String", "TRAKT_CLIENT_SECRET", "\"${localProperties.getProperty("TRAKT_CLIENT_SECRET", "")}\"")
        buildConfigField("String", "TRAKT_API_URL", "\"${localProperties.getProperty("TRAKT_API_URL", "https://api.trakt.tv/")}\"")
        buildConfigField("String", "TV_LOGIN_WEB_BASE_URL", "\"${localProperties.getProperty("TV_LOGIN_WEB_BASE_URL", "https://app.nuvio.tv/tv-login")}\"")

        // In-app updater (GitHub Releases)
        buildConfigField("String", "GITHUB_OWNER", "\"tapframe\"")
        buildConfigField("String", "GITHUB_REPO", "\"NuvioTV\"")
    }

    signingConfigs {
        val keystoreFile = rootProject.file("nuviotv.jks")
        if (keystoreFile.exists()) {
            create("release") {
                keyAlias = "nuviotv"
                keyPassword = "815787"
                storeFile = keystoreFile
                storePassword = "815787"
            }
        }
    }

    buildTypes {
        debug {
            isDebuggable = true
            isMinifyEnabled = false

            buildConfigField("boolean", "IS_DEBUG_BUILD", "true")

            // Dev environment (from local.dev.properties)
            buildConfigField("String", "SUPABASE_URL", "\"${devProperties.getProperty("SUPABASE_URL", "")}\"")
            buildConfigField("String", "SUPABASE_ANON_KEY", "\"${devProperties.getProperty("SUPABASE_ANON_KEY", "")}\"")
            buildConfigField("String", "TV_LOGIN_WEB_BASE_URL", "\"${devProperties.getProperty("TV_LOGIN_WEB_BASE_URL", "https://app.nuvio.tv/tv-login")}\"")
            buildConfigField("String", "PARENTAL_GUIDE_API_URL", "\"${devProperties.getProperty("PARENTAL_GUIDE_API_URL", "")}\"")
            buildConfigField("String", "INTRODB_API_URL", "\"${devProperties.getProperty("INTRODB_API_URL", "")}\"")
            buildConfigField("String", "TRAILER_API_URL", "\"${devProperties.getProperty("TRAILER_API_URL", "")}\"")
            buildConfigField("String", "IMDB_RATINGS_API_BASE_URL", "\"${devProperties.getProperty("IMDB_RATINGS_API_BASE_URL", "")}\"")
        }
        release {
            if (signingConfigs.findByName("release") != null) {
                signingConfig = signingConfigs.getByName("release")
            }
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )

            buildConfigField("boolean", "IS_DEBUG_BUILD", "false")

            // Production environment (from local.properties)
            buildConfigField("String", "SUPABASE_URL", "\"${localProperties.getProperty("SUPABASE_URL", "")}\"")
            buildConfigField("String", "SUPABASE_ANON_KEY", "\"${localProperties.getProperty("SUPABASE_ANON_KEY", "")}\"")
            buildConfigField("String", "TV_LOGIN_WEB_BASE_URL", "\"${localProperties.getProperty("TV_LOGIN_WEB_BASE_URL", "https://app.nuvio.tv/tv-login")}\"")
            buildConfigField("String", "PARENTAL_GUIDE_API_URL", "\"${localProperties.getProperty("PARENTAL_GUIDE_API_URL", "")}\"")
            buildConfigField("String", "INTRODB_API_URL", "\"${localProperties.getProperty("INTRODB_API_URL", "")}\"")
            buildConfigField("String", "TRAILER_API_URL", "\"${localProperties.getProperty("TRAILER_API_URL", "")}\"")
            buildConfigField("String", "IMDB_RATINGS_API_BASE_URL", "\"${localProperties.getProperty("IMDB_RATINGS_API_BASE_URL", "")}\"")
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    buildFeatures {
        compose = true
        buildConfig = true
    }
}

// Fixed Kotlin JVM target configuration for AGP 9.0
kotlin {
    jvmToolchain(11)
}

composeCompiler {
    metricsDestination = layout.buildDirectory.dir("compose_metrics")
    reportsDestination = layout.buildDirectory.dir("compose_reports")
    stabilityConfigurationFiles.add(rootProject.layout.projectDirectory.file("compose_stability_config.conf"))
}

configurations.all {
    exclude(group = "androidx.media3", module = "media3-exoplayer")
    exclude(group = "androidx.media3", module = "media3-ui")
}

dependencies {
    implementation(project(":shared"))
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.profileinstaller)
    implementation("androidx.recyclerview:recyclerview:1.4.0")
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation("androidx.compose.material3:material3")
    implementation("androidx.compose.foundation:foundation")
    implementation("androidx.compose.material:material-icons-extended")
    implementation(libs.androidx.tv.foundation)
    implementation(libs.androidx.tv.material)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)

    // Hilt
    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)
    implementation(libs.hilt.navigation.compose)

    // Networking
    implementation(libs.retrofit)
    implementation(libs.retrofit.moshi)
    implementation(libs.okhttp)
    implementation(libs.okhttp.logging)
    implementation(libs.moshi)
    ksp(libs.moshi.codegen)

    // Coroutines
    implementation(libs.coroutines.core)
    implementation(libs.coroutines.android)

    // Image Loading
    implementation(libs.coil.compose)
    implementation(libs.coil.svg)

    // Navigation
    implementation(libs.navigation.compose)

    // DataStore
    implementation(libs.datastore.preferences)

    // ViewModel
    implementation(libs.lifecycle.viewmodel.compose)

    implementation(libs.media3.exoplayer.hls)
    implementation(libs.media3.exoplayer.dash)
    implementation(libs.media3.exoplayer.smoothstreaming)
    implementation(libs.media3.exoplayer.rtsp)
    implementation(libs.media3.datasource)
    implementation(libs.media3.datasource.okhttp)
    implementation(libs.media3.decoder)
    implementation(libs.media3.session)
    implementation(libs.media3.common)
    implementation(libs.media3.container)
    implementation(libs.media3.extractor)

    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("lib-*.aar"))))

    implementation("io.github.peerless2012:ass-media:0.4.0-beta01")
    implementation("dev.chrisbanes.haze:haze-android:0.7.3") {
        exclude(group = "org.jetbrains.compose.ui")
        exclude(group = "org.jetbrains.compose.foundation")
    }

    implementation(libs.quickjs.kt)
    implementation(libs.jsoup)
    implementation(libs.gson)

    implementation(libs.markdown.renderer.m3)

    implementation(libs.crypto.js)
    implementation(libs.nanohttpd)
    implementation(libs.zxing.core)

    implementation(platform(libs.supabase.bom))
    implementation(libs.supabase.auth)
    implementation(libs.supabase.postgrest)
    implementation(libs.ktor.client.okhttp)

    implementation(libs.kotlinx.serialization.json)

    implementation("org.webjars.npm:crypto-js:4.2.0")

    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    testImplementation("junit:junit:4.13.2")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.8.1")
    testImplementation("io.mockk:mockk:1.13.12")
    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.test.manifest)
}
