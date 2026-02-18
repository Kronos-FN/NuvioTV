plugins {
    kotlin("multiplatform") version "1.6.10"
}

kotlin {
    jvm { }
    sourceSets {
        val jvmMain by getting {
            dependencies {
                implementation("org.jetbrains.compose.desktop:desktop:1.0.0")
            }
        }
    }
}

tasks.withType(org.jetbrains.kotlin.gradle.tasks.KotlinCompile::class) {
    kotlinOptions.jvmTarget = "11"
}