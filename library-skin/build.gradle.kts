@file:Suppress("UnstableApiUsage", "DSL_SCOPE_VIOLATION")

import kotlin.io.println
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Properties
import java.io.FileInputStream

plugins {
    alias(libs.plugins.android.module)
    alias(libs.plugins.kotlin.android)
    id("kotlin-kapt")
}

android {
    namespace = "com.pp.skin"
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

    // 方便开发时预览不同皮肤效果
    sourceSets {
        var theme = "default"
        if ("com.android.library" == libs.plugins.android.module.get().pluginId) {
//        theme = "black"
//            theme = "blue"
        }
        when (theme) {
            "black" -> get("main").res.srcDir("src/skinBlack/res")
            "blue" -> get("main").res.srcDir("src/skinBlue/res")
            else -> get("main").res.srcDir("src/main/res-skin")
        }
    }

/*


    applicationVariants.all {
        this@all.outputs.all {
            val apkName = this.baseName.split("-")[0]
            val customOutPutFile = "${apkName}.skin"
            outputFile.renameTo(File(customOutPutFile))
        }
//        println("${rootDir.absolutePath}/apk/${buildType.name}")
    }

    //配置皮肤包
    flavorDimensionList.add("skin")
    productFlavors {
        create("skinBlack") {
//            defaultConfig.applicationId = "com.pp.skin_black"
//            android.namespace = "com.pp.skin_black"
            signingConfig = signingConfigs["release"]
            dimension = "skin"
        }

        create("skinBlue") {
//            defaultConfig.applicationId = "com.pp.skin_blue"
//            android.namespace = "com.pp.skin_blue"
            signingConfig = signingConfigs["release"]
            dimension = "skin"
        }

    }

*/


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

    implementation(libs.material.get())

}