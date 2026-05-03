plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.hilt)
    alias(libs.plugins.kotlin.ksp)
    alias(libs.plugins.compose.compiler)
}

android {
    namespace = "com.onemusic"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.onemusic"
        minSdk = 26
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }
    kotlin {
        compilerOptions {
            jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_21)
        }
    }
    buildFeatures {
        compose = true
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    implementation(project(":innertube"))
    implementation(project(":betterlyrics"))
    implementation(project(":lrclib"))
    implementation(project(":kizzy"))
    implementation(project(":kugou"))

    implementation(libs.activity)
    implementation(libs.compose.ui)
    implementation(libs.compose.foundation)
    implementation(libs.material3)
    implementation(libs.compose.material.icons)
    implementation(libs.material.icons.extended)
    implementation(libs.coil)
    implementation(libs.media3)
    implementation(libs.media3.session)
    implementation(libs.hilt)
    implementation(libs.viewmodel.compose)
    implementation(libs.navigation)
    implementation(libs.hilt.navigation)

    ksp(libs.hilt.compiler)

    testImplementation(libs.junit)
}
