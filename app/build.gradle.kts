plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.hilt)
    alias(libs.plugins.ksp)
}

android {
    namespace = "com.example.novaplayer"

    compileSdk {
        version = release(37) {
            minorApiLevel = 1
        }
    }

    defaultConfig {
        applicationId = "com.example.novaplayer"
        minSdk = 23
        targetSdk = 36

        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner =
            "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            optimization {
                enable = false
            }
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    buildFeatures {
        compose = true
    }
}

dependencies {

    // =========================
    // Compose
    // =========================

    implementation(platform(libs.androidx.compose.bom))

    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.compose.foundation)

    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.material.icons.extended)

    implementation(libs.androidx.compose.ui)

    implementation(libs.androidx.compose.ui.graphics)

    implementation(libs.androidx.compose.ui.tooling.preview)

    debugImplementation(libs.androidx.compose.ui.tooling)


    // =========================
    // Android Core
    // =========================

    implementation(libs.androidx.core.ktx)

    implementation(libs.androidx.lifecycle.runtime.ktx)


    // =========================
    // Media3
    // =========================

    implementation(libs.androidx.media3.exoplayer)

    implementation(libs.androidx.media3.session)


    // =========================
    // Concurrent
    // =========================

    implementation(libs.androidx.concurrent.ktx)


    // =========================
    // Hilt
    // =========================

    implementation(libs.hilt.android)

    implementation(libs.androidx.hilt.navigation.compose)

    ksp(libs.hilt.compiler)


    // =========================
    // Kotlin Serialization
    // =========================

    implementation(libs.kotlinx.serialization.json)


    // =========================
    // Navigation
    // =========================

    implementation(libs.androidx.navigation.compose)


    // =========================
    // Unit Tests
    // =========================

    testImplementation(libs.junit)


    // =========================
    // Android Tests
    // =========================

    androidTestImplementation(platform(libs.androidx.compose.bom))

    androidTestImplementation(libs.androidx.compose.ui.test.junit4)

    androidTestImplementation(libs.androidx.espresso.core)

    androidTestImplementation(libs.androidx.junit)

    debugImplementation(libs.androidx.compose.ui.test.manifest)


    // =========================
    // DataStore
    // =========================

    implementation(libs.androidx.datastore.preferences)


    // =========================
    // Coil
    // =========================
    implementation(libs.coil.compose)

    implementation(libs.backdrop)



}