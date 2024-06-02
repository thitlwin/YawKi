object Lib {
    object Android {
        private const val COMPOSE_NAVIGATION_VERSION = "2.7.7"
        const val COMPOSE_VERSION = "1.5.2"
        private const val COMPOSE_UI_FONT_VERSION = "1.6.7"
        private const val COMPOSE_COIL_VERSION = "2.6.0"

        const val COMPOSE_NAVIGATION =
            "androidx.navigation:navigation-compose:${COMPOSE_NAVIGATION_VERSION}"

        const val COMPOSE_BOM = "androidx.compose:compose-bom:2023.08.00"
        const val COMPOSE_UI = "androidx.compose.ui:ui"
        const val COMPOSE_UI_GRAPHICS = "androidx.compose.ui:ui-graphics"
        const val COMPOSE_UI_TOOLING_PREVIEW = "androidx.compose.ui:ui-tooling-preview"
        const val COMPOSE_UI_TOOLING = "androidx.compose.ui:ui-tooling"
        const val COMPOSE_MATERIAL3 = "androidx.compose.material3:material3"
        const val COMPOSE_MATERIAL2 = "androidx.compose.material:material"
        const val COMPOSE_UI_FONT =
            "androidx.compose.ui:ui-text-google-fonts:${COMPOSE_UI_FONT_VERSION}"
        const val COMPOSE_COIL = "io.coil-kt:coil-compose:${COMPOSE_COIL_VERSION}"

        const val GOOGLE_MATERIAL = "com.google.android.material:material:1.12.0"
    }

    object Media3 {
        private const val MEDIA_VERSION = "1.3.1"
        const val EXOPLAYER = "androidx.media3:media3-exoplayer:${MEDIA_VERSION}"
        const val MEDIA3_SESSION = "androidx.media3:media3-session:${MEDIA_VERSION}"
    }

    object Firebase {
        const val FIREBASE_BOM = "com.google.firebase:firebase-bom:33.0.0"
        const val FIREBASE_ANALYTICS = "com.google.firebase:firebase-analytics"
        const val CLOUD_FIRESTORE = "com.google.firebase:firebase-firestore"
    }

    object Kotlin {
        const val KOTLIN_VERSION = "1.7.10"
        private const val KTX_CORE_VERSION = "1.13.1"

        const val KTX_CORE = "androidx.core:core-ktx:${KTX_CORE_VERSION}"
        const val KT_STD = "org.jetbrains.kotlin:kotlin-stdlib-jdk8:${KOTLIN_VERSION}"
    }

    object Di {
        const val HILT_VERSION = "2.46"
        const val HILT_COMPILER_VERSION = "1.2.0"
        const val hiltAndroid = "com.google.dagger:hilt-android:${HILT_VERSION}"
        const val hiltCompiler = "androidx.hilt:hilt-compiler:${HILT_COMPILER_VERSION}"

        const val hiltAndroidCompiler = "com.google.dagger:hilt-android-compiler:${HILT_VERSION}"
        const val hiltNavigationCompose = "androidx.hilt:hilt-navigation-compose:1.2.0"
    }
}

object BuildPlugins {
    const val ANDROID_APPLICATION_PLUGIN = "com.android.application"
    const val ANDROID_LIBRARY_PLUGIN = "com.android.library"
    const val KOTLIN_ANDROID_PLUGIN = "org.jetbrains.kotlin.android"
    const val KOTLIN_KAPT = "kotlin-kapt"
    const val KOTLIN_JVM = "org.jetbrains.kotlin.jvm"
    const val DAGGER_HILT_PLUGIN = "com.google.dagger.hilt.android"
    const val GOOGLE_SERVICE = "com.google.gms.google-services"
}