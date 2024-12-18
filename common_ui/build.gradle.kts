plugins {
    id(BuildPlugins.ANDROID_LIBRARY_PLUGIN)
    id(BuildPlugins.KOTLIN_ANDROID_PLUGIN)
//    id(BuildPlugins.GOOGLE_SERVICE)
}

android {
    namespace = "com.yawki.common_ui"
    compileSdk = ProjectProperties.COMPILE_SDK

    defaultConfig {
        minSdk = ProjectProperties.MIN_SDK

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
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
        kotlinCompilerExtensionVersion = "1.5.1"
    }
}

dependencies {
    /* Android Designing and layout */
    api(platform(Lib.Android.COMPOSE_BOM))
    api(Lib.Android.COMPOSE_UI)
    api(Lib.Android.COMPOSE_COIL)

    api(Lib.Android.COMPOSE_MATERIAL2)
    api(Lib.Android.COMPOSE_MATERIAL3)
    implementation(Lib.Android.COMPOSE_UI_FONT)

    implementation("androidx.core:core-ktx:1.13.1")

    implementation(project(":navigator"))
    implementation(project(":common"))
//    api(Lib.Android.GOOGLE_MATERIAL)

    // for firebase auth ui
    implementation(Lib.Firebase.FIREBASE_AUTHENTICATION)
    implementation("com.firebaseui:firebase-ui-auth:7.2.0")
    implementation("androidx.activity:activity-compose:1.9.0")
    // For Google Authentication
//    implementation(Lib.Firebase.GOOGLE_SERVICE_AUTH)

    debugImplementation(Lib.Android.COMPOSE_UI_TOOLING)
}