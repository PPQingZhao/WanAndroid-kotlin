@file:Suppress("UnstableApiUsage", "DSL_SCOPE_VIOLATION")

plugins {
    alias(libs.plugins.kotlin.android)
    id("com.android.library") version (libs.versions.androidGradlePlugin)
    id("kotlin-kapt")
}

android {
    namespace = "com.pp.common"
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

    testImplementation(libs.junit.get())
    androidTestImplementation(libs.ext.junit.get())
    androidTestImplementation(libs.espresso.core.get())

    // 路由
    implementation(libs.arouter.api)
    kapt(libs.arouter.compiler)

    debugApi(libs.leakcanary.get())
    api(libs.kotlin.core.get())
    api(projects.libraryBase)
    api(projects.libraryRouterService)
    api(projects.libraryDatabase)
    api(projects.libraryNetwork)
}