@file:Suppress("UnstableApiUsage", "DSL_SCOPE_VIOLATION")

plugins {
    alias(libs.plugins.kotlin.android)
    id("com.android.application") version (libs.versions.androidGradlePlugin)
//    id("kotlin-kapt")
}

android {
    namespace = "com.pp.wanandroid_kotlin"
    compileSdk = libs.versions.compileSdk.get().toInt()

    defaultConfig {
        applicationId = "com.pp.wanandroid_kotlin"
        minSdk = libs.versions.minSdk.get().toInt()
        targetSdk = libs.versions.targetSdk.get().toInt()
        versionCode = libs.versions.versionCode.get().toInt()
        versionName = libs.versions.versionName.get().toString()

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {

    implementation(libs.kotlin.core.get())
    implementation(libs.appcompat.get())
    implementation(libs.material.get())
    implementation(libs.constraintlayout.get())
    testImplementation(libs.junit.get())
    androidTestImplementation(libs.ext.junit.get())
    androidTestImplementation(libs.espresso.core.get())
}