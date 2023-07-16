@file:Suppress("UnstableApiUsage")

pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
//    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
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
    ":module-main",
    ":skin-blue",
    ":skin-black",
)