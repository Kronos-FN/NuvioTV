plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.jetbrains.compose)
    alias(libs.plugins.kotlin.compose)
}

kotlin {
    jvm()

    sourceSets {
        val jvmMain by getting {
            dependencies {
                implementation(compose.desktop.currentOs)
                implementation(compose.runtime)
                implementation(compose.foundation)
                implementation(compose.material3)
                implementation(compose.ui)
                implementation(libs.coroutines.core)
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-swing:1.9.0") // Required for Dispatchers.Main on Desktop
                implementation(libs.koin.core)
                implementation("io.insert-koin:koin-compose:1.1.0")
                implementation(libs.vlcj)
                
                // Ktor for Multiplatform Networking
                implementation("io.ktor:ktor-client-core:3.0.1")
                implementation("io.ktor:ktor-client-java:3.0.1")

                implementation(project(":shared"))
            }
        }
    }
}

compose.desktop {
    application {
        mainClass = "com.nuvio.tv.desktop.MainKt"
        nativeDistributions {
            targetFormats(org.jetbrains.compose.desktop.application.dsl.TargetFormat.Exe)
            packageName = "NuvioTV"
            packageVersion = "0.3.6"
            vendor = "tapframe"
            description = "Nuvio Media Hub for Windows"
            
            windows {
                shortcut = true
                // menuGroup = "Nuvio"
            }
        }
    }
}

// Task to copy the built EXE to the root folder for easy finding
tasks.register<Copy>("copyExeToRoot") {
    // Ensuring it runs after the packaging task
    dependsOn("packageExe") 
    
    val buildDir = layout.buildDirectory.get().asFile
    from(File(buildDir, "compose/binaries/main/exe"))
    include("*.exe")
    into(rootProject.projectDir)
    
    // Rename to a consistent name
    rename { "NuvioTV_Installer.exe" }
    
    doLast {
        println("SUCCESS: NuvioTV_Installer.exe has been copied to: ${rootProject.projectDir}")
    }
}
