//apply(from = "${rootProject.projectDir}/yawki_library.gradle.kts")

plugins {
    id(BuildPlugins.ANDROID_APPLICATION_PLUGIN)
    id(BuildPlugins.KOTLIN_ANDROID_PLUGIN)
    id(BuildPlugins.KOTLIN_KAPT)
    id(BuildPlugins.DAGGER_HILT_PLUGIN)
    id(BuildPlugins.GOOGLE_SERVICE)
}

android {
    namespace = "com.thit.yawki"
    compileSdk = ProjectProperties.COMPILE_SDK

    defaultConfig {
        applicationId = ProjectProperties.APPLICATION_ID
        minSdk = ProjectProperties.MIN_SDK
        targetSdk = ProjectProperties.TARGET_SDK
        versionCode = ProjectProperties.VERSION_CODE
        versionName = ProjectProperties.VERSION_NAME

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
kapt {
    correctErrorTypes = true
}
dependencies {
    implementation(project(":ui-dashboard"))
    implementation(project(":navigator"))
    implementation(project(":common"))
    implementation(project(":common_ui"))
    implementation(project(":features:player"))


//    Hilt
    implementation(Lib.Di.hiltAndroid)
    kapt(Lib.Di.hiltCompiler)
    kapt(Lib.Di.hiltAndroidCompiler)

    implementation(Lib.Kotlin.KT_STD)

    implementation("androidx.core:core-ktx:1.13.1")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.8.0")
    implementation("androidx.activity:activity-compose:1.9.0")

    // Compose
    implementation(Lib.Android.COMPOSE_NAVIGATION)
    implementation(platform(Lib.Android.COMPOSE_BOM))
    implementation(Lib.Android.COMPOSE_UI)
    implementation(Lib.Android.COMPOSE_UI_GRAPHICS)
    implementation(Lib.Android.COMPOSE_MATERIAL3)

    // Firebase
    implementation(platform(Lib.Firebase.FIREBASE_BOM))
    implementation(Lib.Firebase.CLOUD_FIRESTORE)
    implementation(Lib.Firebase.FIREBASE_ANALYTICS)
}