import org.jetbrains.compose.ExperimentalComposeLibrary

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsCompose)
    alias(libs.plugins.kotlinxSerialization)
    alias(libs.plugins.mockmp)
}

kotlin {
    androidTarget {
        compilations.all {
            kotlinOptions {
                jvmTarget = "11"
            }
        }
    }

    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64(),
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "SharedKmp"
            isStatic = true
        }
    }

    sourceSets {
        androidMain.dependencies {
            implementation(libs.compose.ui)
            implementation(libs.compose.ui.tooling.preview)
            implementation(libs.androidx.activity.compose)
            implementation(libs.ktor.client.okhttp)
            implementation(libs.napier)
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
        commonTest.dependencies {
            implementation(libs.kotest.framework.engine)
            implementation(libs.kotest.assertions.core)
            implementation(libs.kotest.property)
            implementation(libs.turbine)
            implementation(libs.kotlinx.coroutines.test)
        }
    }
}

android {
    namespace = AppConfig.PACKAGE_NAME
    compileSdk = 34

    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
    sourceSets["main"].res.srcDirs("src/androidMain/res")
    sourceSets["main"].resources.srcDirs("src/commonMain/resources")

    defaultConfig {
        applicationId = AppConfig.PACKAGE_NAME
        minSdk = 24
        targetSdk = 34
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    dependencies {
        debugImplementation(libs.compose.ui.tooling)
    }
}

mockmp {
    usesHelper = true
    installWorkaround()
}

afterEvaluate {
    tasks.withType<AbstractTestTask> {
        testLogging {
            events("passed", "skipped", "failed", "standard_out", "standard_error")
            showExceptions = true
            showStackTraces = true
        }
    }
}

object AppConfig {
    private const val MAJOR = 1
    private const val MINOR = 1
    private const val BUILD = 0

    const val PACKAGE_NAME = "com.jetbrains.mybirdapp"
    const val VERSION_NAME = "$MAJOR.$MINOR.$BUILD"
    val VERSION_BUILD_NUMBER: Int = "${MAJOR}${MINOR.format()}${BUILD.format()}".toInt()

    private fun Int.format() = "%03d".format(this)
}
