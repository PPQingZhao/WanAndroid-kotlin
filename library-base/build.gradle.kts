@file:Suppress("UnstableApiUsage", "DSL_SCOPE_VIOLATION")

plugins {
    alias(libs.plugins.kotlin.android)
    id("com.android.library") version (libs.versions.androidGradlePlugin)
    id("kotlin-kapt")
}

android {
    namespace = "com.pp.base"
    compileSdk = libs.versions.compileSdk.get().toInt()

    defaultConfig {
        minSdk = libs.versions.minSdk.get().toInt()
        targetSdk = libs.versions.targetSdk.get().toInt()

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        javaCompileOptions {
            annotationProcessorOptions {
                arguments["AROUTER_MODULE_NAME"] = project.name
            }
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    dataBinding {
        enable = true
    }
}

dependencies {

    implementation(libs.kotlin.core.get())
    implementation(libs.appcompat.get())
    testImplementation(libs.junit.get())
    androidTestImplementation(libs.ext.junit.get())
    androidTestImplementation(libs.espresso.core.get())

    api(projects.libraryMvvm)
    api(projects.libraryTheme)
    api(projects.libraryUi)

    api(libs.datastore)
    api(libs.palette)

}