object Lib {
    object Android {
        private const val COMPOSE_NAVIGATION_VERSION = "2.7.7"

        const val COMPOSE_NAVIGATION =
            "androidx.navigation:navigation-compose:${COMPOSE_NAVIGATION_VERSION}"

    }
    object Kotlin {
        const val KOTLIN_VERSION = "1.7.10"
        private const val KTX_CORE_VERSION = "1.13.1"

        const val KTX_CORE = "androidx.core:core-ktx:${KTX_CORE_VERSION}"
        const val KT_STD = "org.jetbrains.kotlin:kotlin-stdlib-jdk8:${KOTLIN_VERSION}"
    }

    object Di {
        const val HILT_VERSION = "2.46"
        const val hiltAndroid = "com.google.dagger:hilt-android:${HILT_VERSION}"
        const val hiltAndroidCompiler = "com.google.dagger:hilt-android-compiler:${HILT_VERSION}"
        const val hiltNavigationCompose = "androidx.hilt:hilt-navigation-compose:1.2.0"
    }
}

object BuildPlugins {
    const val ANDROID_APPLICATION_PLUGIN = "com.android.application"
    const val ANDROID_LIBRARY_PLUGIN = "com.android.library"
    const val KOTLIN_ANDROID_PLUGIN = "org.jetbrains.kotlin.android"
    const val KOTLIN_KAPT = "kotlin-kapt"
    const val KOTLIN_JVM="org.jetbrains.kotlin.jvm"
    const val DAGGER_HILT_PLUGIN = "com.google.dagger.hilt.android"
}