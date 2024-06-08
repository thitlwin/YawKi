plugins {
    id(BuildPlugins.ANDROID_LIBRARY_PLUGIN)
    id(BuildPlugins.KOTLIN_ANDROID_PLUGIN)
    id(BuildPlugins.KOTLIN_KAPT)
}

android {
    namespace = "com.yawki.player"
    compileSdk = ProjectProperties.COMPILE_SDK

    defaultConfig {
        minSdk = ProjectProperties.MIN_SDK

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
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
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = Lib.Android.COMPOSE_VERSION
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    implementation(project(":common"))
    implementation(project(":common_ui"))
    implementation(project(":navigator"))

    // For DI
    kapt(Lib.Di.hiltAndroidCompiler)
    implementation(Lib.Di.hiltNavigationCompose)

    debugImplementation(Lib.Android.COMPOSE_UI_TOOLING)

    // For Unit Testing
    testImplementation(Lib.UnitTesting.mockitoCore)
    testImplementation(Lib.UnitTesting.jUnit4)
    testImplementation(Lib.UnitTesting.assertjCore)

    // For Kotlin Coroutine Test
    testImplementation(Lib.Kotlin.COROUTINE_TEST)

}