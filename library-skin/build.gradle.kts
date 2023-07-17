@file:Suppress("UnstableApiUsage", "DSL_SCOPE_VIOLATION")

import kotlin.io.println

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

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
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
        var applicationId = ""
        create("skinBlack") {
            applicationId = "com.pp.skin_black"
            dimension = "skin"
        }

        create("skinBlue") {
            applicationId = "com.pp.skin_blue"
            dimension = "skin"
        }

        if ("com.android.application" == libs.plugins.android.module.get().pluginId) {
            defaultConfig.applicationId = applicationId
        }
    }
*/

    // 方便开发时预览不同皮肤效果
    sourceSets {
        var theme = "default"
//        theme = "black"
        theme = "blue"
        when (theme) {
            "black" -> get("main").res.srcDir("src/skinBlack/resources")
            "blue" -> get("main").res.srcDir("src/skinBlue/resources")
            else -> get("main").res.srcDir("src/main/res-skin")
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

android{

}

dependencies {

    implementation(libs.material.get())

}