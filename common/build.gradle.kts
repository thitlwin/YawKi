plugins {
    id(BuildPlugins.ANDROID_LIBRARY_PLUGIN)
    id(BuildPlugins.KOTLIN_ANDROID_PLUGIN)
    id(BuildPlugins.KOTLIN_KAPT)
    id(BuildPlugins.DAGGER_HILT_PLUGIN)
}

android {
    namespace = "com.yawki.common"
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
}

dependencies {

// Firebase
    implementation(platform(Lib.Firebase.FIREBASE_BOM))
    implementation(Lib.Firebase.CLOUD_FIRESTORE)

// For DI
    implementation(Lib.Di.hiltAndroid)
    kapt(Lib.Di.hiltCompiler)
    kapt(Lib.Di.hiltAndroidCompiler)

//    FOR MEDIA
    implementation(Lib.Media3.EXOPLAYER)
    implementation(Lib.Media3.MEDIA3_SESSION)

    // For AppCompat
    implementation(Lib.AppCompat.APPCOMPAT_RESOURCES)
    // For Lifecycle
    implementation(Lib.Android.LIFECYCLE_RUNTIME)

    // For Unit Testing
    testImplementation(Lib.UnitTesting.mockk)
    testImplementation(Lib.UnitTesting.jUnit4)
    testImplementation(Lib.UnitTesting.assertjCore)

    // For Hilt Android Testing
    androidTestImplementation(Lib.Di.hiltAndroidTesting)
    kaptAndroidTest(Lib.Di.hiltAndroidCompiler)

    // For Kotlin Coroutine Test
    testImplementation(Lib.Kotlin.COROUTINE_TEST)
    // Flow Testing
    testImplementation(Lib.Turbine.Turbine)

}