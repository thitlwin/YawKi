// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    id(BuildPlugins.ANDROID_APPLICATION_PLUGIN) version "8.2.2" apply false
    id(BuildPlugins.KOTLIN_ANDROID_PLUGIN) version "1.9.0" apply false
    id(BuildPlugins.ANDROID_LIBRARY_PLUGIN) version "8.2.2" apply false
    id(BuildPlugins.KOTLIN_JVM) version "1.9.0" apply false
    id(BuildPlugins.DAGGER_HILT_PLUGIN) version Lib.Di.HILT_VERSION apply false
    id(BuildPlugins.GOOGLE_SERVICE) version "4.4.2" apply false
}