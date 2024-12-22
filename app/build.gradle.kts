plugins {
    id("kotlin-android")
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    kotlin("kapt")
}

android {
    namespace = "id.cbm.main.cbm_calculator"
    compileSdk = 34

    defaultConfig {
        applicationId = "id.cbm.main.cbm_calculator"
        minSdk = 29
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        debug {
            isMinifyEnabled = false
            isDebuggable = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
        release {
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    buildFeatures {
        buildConfig = true
        viewBinding = true
    }

    flavorDimensions += "version"
    productFlavors {
        create("dev") {
            dimension = "version"
            applicationId = "id.cbm.main.cbm_calculator.dev"
            versionNameSuffix = "-Dev"
            buildConfigField("Boolean", "LOGGING_ENABLED", "true")
        }

        create("prod") {
            dimension = "version"
            applicationId = "id.cbm.main.cbm_calculator"
            versionNameSuffix = ""
            buildConfigField("Boolean", "LOGGING_ENABLED", "false")
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
    val fragment_version = "1.8.5"
    val coroutine_version = "1.7.3"
    val lifecycle_version = "2.6.2"
    val room_version = "2.6.1"
    val retrofit_version = "2.9.0"
    val material_android = "1.12.0"

    implementation("androidx.core:core-ktx:1.10.1")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.9.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("androidx.core:core-splashscreen:1.0.1")

    implementation("com.google.android.material:material:$material_android")

    // Kotlin
    implementation("androidx.fragment:fragment-ktx:$fragment_version")

    // Coroutines
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutine_version")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:$coroutine_version")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-play-services:$coroutine_version")

    // di
    val koinVersion = "3.5.3"
    implementation("io.insert-koin:koin-android:$koinVersion")

    // Room
    implementation("androidx.room:room-runtime:$room_version")
    // optional - Kotlin Extensions and Coroutines support for Room
    implementation("androidx.room:room-ktx:$room_version")
    kapt("androidx.room:room-compiler:$room_version")

    // Lifecycle
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:$lifecycle_version")
    implementation("androidx.lifecycle:lifecycle-extensions:2.2.0")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:$lifecycle_version")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:$lifecycle_version")
    implementation("androidx.activity:activity-ktx:1.8.2")

    // Retrofit
    implementation("com.squareup.retrofit2:retrofit:$retrofit_version")
    implementation("com.squareup.retrofit2:converter-gson:$retrofit_version")
    implementation("com.squareup.okhttp3:logging-interceptor:4.12.0")
    implementation("com.squareup.retrofit2:converter-scalars:$retrofit_version")

    // Reflection
    implementation("org.jetbrains.kotlin:kotlin-reflect:1.9.10")

    // intuit sdp (scalable density pixel) and ssp (scalable pixel for text)
    implementation("com.intuit.sdp:sdp-android:1.1.1")
    implementation("com.intuit.ssp:ssp-android:1.1.1")

    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
}