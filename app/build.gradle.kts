plugins {
    alias(libs.plugins.jetbrainsKotlinAndroid)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.hilt)
    alias(libs.plugins.ksp)
}

android {
    namespace = "uz.toshmatov.currency"
    compileSdk = 34

    defaultConfig {
        applicationId = "uz.toshmatov.currency"
        minSdk = 21
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

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
            signingConfig = signingConfigs.getByName("debug")
        }

        debug {}
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
        buildConfig = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.3"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {

    // androidx core
    implementation(libs.androidx.core.ktx)

    // androidx lifecycle
    implementation(libs.androidx.lifecycle.viewModelCompose)
    implementation(libs.androidx.lifecycle.runtimeCompose)
    implementation(libs.androidx.lifecycle.livedata.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)

    // androidx compose
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.activity.compose)

    // androidx ui
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.ui)

    // test
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    androidTestImplementation(libs.androidx.espresso.core)
    debugImplementation(libs.androidx.ui.test.manifest)
    androidTestImplementation(libs.androidx.junit)
    debugImplementation(libs.androidx.ui.tooling)
    testImplementation(libs.junit)

    // hilt
    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)

    // kotlin coroutines
    implementation(libs.kotlinx.coroutines.core)

    // kotlin collection
    implementation(libs.kotlinx.collections.immutable)

    // kotlin serialization
    implementation(libs.kotlinx.serialization.json)

    // paging
    implementation(libs.androidx.pager.compose)
    implementation(libs.androidx.pager)

    // voyager
    implementation(libs.voyager.bottomsheet)
    implementation(libs.voyager.transitions)
    implementation(libs.voyager.viewmodel)
    implementation(libs.voyager.hilt)
    implementation(libs.voyager.tab)
    implementation(libs.voyager)

    // okhttp
    implementation(libs.okhttp)

    // retrofit
    implementation(libs.retrofit.kotlin.serialization)
    implementation(libs.retrofit.core)
    implementation(libs.retrofit.gson)

    // gson
    implementation(libs.gson)

    // room_database
    annotationProcessor(libs.room.compiler)
    implementation(libs.room.paging)
    implementation(libs.room.ktx)
    implementation(libs.room)
    ksp(libs.room.ksp.compiler)

    // coil
    implementation(libs.coil.kt.compose)

    // lottie
    implementation(libs.lottie)

    // timber
    implementation(libs.timber)

    // chucker
    implementation(libs.chucker)

    // system ui
    implementation(libs.accompanist.systemui)
    implementation(libs.accompanist.swiperefresh)

    // jsoup
    implementation(libs.jsoup)

    // lingver
    implementation(libs.lingver)

    // datastore
    implementation(libs.androidx.dataStore.core)
    implementation(libs.androidx.dataStore.preferences)

    // appwidget glance
    implementation(libs.androidx.appwidget.glance)
    implementation(libs.androidx.appwidget.glance.material)
}