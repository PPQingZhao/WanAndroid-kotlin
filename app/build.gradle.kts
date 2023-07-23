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
    namespace = "com.pp.wanandroid"
    compileSdk = libs.versions.compileSdk.get().toInt()

    defaultConfig {
        applicationId = "com.pp.wanandroid"
        minSdk = libs.versions.minSdk.get().toInt()
        targetSdk = libs.versions.targetSdk.get().toInt()
        versionCode = libs.versions.versionCode.get().toInt()
        versionName = libs.versions.versionName.get()

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        javaCompileOptions {
            annotationProcessorOptions {
                arguments["AROUTER_MODULE_NAME"] = project.name
            }
        }
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
            val projectName = rootProject.name
            val releaseTime = releaseTime()
            val versionName = defaultConfig.versionName
            val buildType = buildType.name
            val customOutPutFile = "${projectName}_${releaseTime}_${versionName}_${buildType}.apk"
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

    implementation(projects.libraryCommon)
    implementation(projects.moduleMain)

    if ("com.android.library" == libs.plugins.android.module.get().pluginId) {
        implementation(projects.moduleUser)
    }

}