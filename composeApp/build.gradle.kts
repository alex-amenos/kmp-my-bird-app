import org.jetbrains.compose.ExperimentalComposeLibrary
import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsCompose)
    alias(libs.plugins.kotlinxSerialization)
}

kotlin {
    androidTarget {
        compilations.all {
            kotlinOptions {
                jvmTarget = AppConfig.JVM_TARGET
            }
        }
    }

    jvm("desktop")

    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64(),
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = AppConfig.SHARED_BINARY_NAME
            isStatic = true
        }
    }

    sourceSets {
        val desktopMain by getting

        androidMain.dependencies {
            implementation(libs.compose.ui)
            implementation(libs.compose.ui.tooling.preview)
            implementation(libs.androidx.activity.compose)
            implementation(libs.ktor.client.okhttp)
            implementation(libs.napier)
        }
        desktopMain.dependencies {
            implementation(compose.desktop.currentOs)
            implementation(libs.kotlinx.coroutines.swing)
            implementation(libs.ktor.client.okhttp)
        }
        iosMain.dependencies {
            implementation(libs.ktor.client.darwin)
        }
        commonMain.dependencies {
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material)
            @OptIn(ExperimentalComposeLibrary::class)
            implementation(compose.components.resources)

            implementation(libs.arrow.core)
            implementation(libs.kamel)
            implementation(libs.bundles.ktor.common)
            implementation(libs.moko.mvvm.core)
            implementation(libs.moko.mvvm.compose)
            implementation(libs.napier)
        }
    }
}

android {
    namespace = AppConfig.PACKAGE_NAME
    compileSdk = AppConfig.ANDROID_COMPILE_SDK

    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
    sourceSets["main"].res.srcDirs("src/androidMain/res")
    sourceSets["main"].resources.srcDirs("src/commonMain/resources")

    defaultConfig {
        applicationId = AppConfig.PACKAGE_NAME
        minSdk = AppConfig.ANDROID_MIN_SDK
        targetSdk = AppConfig.ANDROID_TARGET_SDK
        versionCode = AppConfig.VERSION_BUILD_NUMBER
        versionName = AppConfig.VERSION_NAME
    }
    buildFeatures {
        buildConfig = true
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.compose.compiler.get()
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }
    compileOptions {
        sourceCompatibility = AppConfig.JAVA_VERSION
        targetCompatibility = AppConfig.JAVA_VERSION
    }
    dependencies {
        debugImplementation(libs.compose.ui.tooling)
    }
}

compose.desktop {
    application {
        mainClass = "MainKt"

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = AppConfig.PACKAGE_NAME
            packageVersion = AppConfig.VERSION_NAME
        }
    }
}

object AppConfig {
    private const val MAJOR = 1
    private const val MINOR = 1
    private const val BUILD = 0

    const val SHARED_BINARY_NAME = "SharedKmp"
    const val ANDROID_MIN_SDK: Int = 24
    const val ANDROID_COMPILE_SDK: Int = 34
    const val ANDROID_TARGET_SDK: Int = 34
    const val JVM_TARGET = "11"
    const val PACKAGE_NAME = "com.jetbrains.mybirdapp"
    const val VERSION_NAME = "$MAJOR.$MINOR.$BUILD"
    val JAVA_VERSION = JavaVersion.VERSION_11
    val VERSION_BUILD_NUMBER: Int = "${MAJOR}${MINOR.format()}${BUILD.format()}".toInt()

    private fun Int.format() = "%03d".format(this)
}
