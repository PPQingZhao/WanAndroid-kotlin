@file:Suppress("UnstableApiUsage", "DSL_SCOPE_VIOLATION")

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Properties
import java.io.FileInputStream

plugins {
    alias(libs.plugins.kotlin.android)
    id("com.android.application") version (libs.versions.androidGradlePlugin)
    id("kotlin-kapt")
}

fun releaseTime(): String {
    return SimpleDateFormat("yyyyMMdd").format(Date())
}

android {
    namespace = "com.pp.skin_black"
    compileSdk = libs.versions.compileSdk.get().toInt()

    defaultConfig {
        minSdk = libs.versions.minSdk.get().toInt()
        targetSdk = libs.versions.targetSdk.get().toInt()

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    signingConfigs {
        create("release") {
            val signedProperties = Properties()
            signedProperties.load(FileInputStream(rootProject.file("local.properties")))
            storeFile = file(signedProperties.getProperty("storeFile"))
            storePassword = signedProperties.getProperty("storePassword")
            keyAlias = signedProperties.getProperty("keyAlias")
            keyPassword = signedProperties.getProperty("keyPassword")
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

    applicationVariants.all {
        outputs.all {
            val projectName = project.name
            val customOutPutFile = "${projectName}.skin"
            outputFile.renameTo(File(customOutPutFile))
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
    api(libs.material.get())
}