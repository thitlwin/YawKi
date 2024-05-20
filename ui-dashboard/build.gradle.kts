plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id(BuildPlugins.KOTLIN_KAPT)
}

android {
    namespace = "com.yawki.ui_dashboard"
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
        kotlinCompilerExtensionVersion = Lib.Android.COMPOSE_VERSION
    }
}

dependencies {
    implementation(project(":common"))

    implementation(project(":navigator"))
    implementation(project(":common_ui"))

    api(Lib.Di.hiltNavigationCompose)
    api(Lib.Android.COMPOSE_UI)
    api(Lib.Android.COMPOSE_MATERIAL2)
    api(Lib.Android.COMPOSE_UI_TOOLING_PREVIEW)

    /*DI*/
//    api(Lib.Di.hiltAndroid)
//    api(Lib.Di.hiltNavigationCompose)
//    kapt(Lib.Di.hiltCompiler)
    kapt(Lib.Di.hiltAndroidCompiler)


    implementation("androidx.core:core-ktx:1.13.1")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.12.0")
//    implementation("androidx.navigation:navigation-common-ktx:2.7.7")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
}