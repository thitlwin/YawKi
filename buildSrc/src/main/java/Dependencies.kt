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
}

object BuildPlugins {
    const val ANDROID_APPLICATION_PLUGIN = "com.android.application"
    const val ANDROID_LIBRARY_PLUGIN = "com.android.library"
    const val KOTLIN_ANDROID_PLUGIN = "org.jetbrains.kotlin.android"
    const val KOTLIN_KAPT = "kotlin-kapt"
    const val KOTLIN_JVM="org.jetbrains.kotlin.jvm"
}