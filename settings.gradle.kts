@file:Suppress("UnstableApiUsage")

pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
        maven("https://maven.aliyun.com/repository/public")
        maven("https://jitpack.io")
    }
}
dependencyResolutionManagement {
//    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        maven("https://maven.aliyun.com/repository/public")
        maven("https://jitpack.io")
        google()
        mavenCentral()
    }
}

enableFeaturePreview("VERSION_CATALOGS")

// Generate type safe accessors when referring to other projects eg.
// Before: implementation(project(":feature-album"))
// After: implementation(projects.featureAlbum)
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

rootProject.name = "WanAndroid"

include(
    ":app",
    ":library-common",
    ":library-theme",
    ":library-network",
    ":library-mvvm",
    ":library-base",
    ":library-ui",
    ":library-skin",
    ":library-router-service",
    ":library-database",
    ":module-main",
    ":module-user",
    ":module-local",
    ":module-home",
    ":module-project",
    ":module-navigation",
)