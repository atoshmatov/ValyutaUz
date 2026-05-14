plugins {
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.hilt)
    alias(libs.plugins.ksp)
    alias(libs.plugins.ktlint)
    id("kotlin-parcelize")
}

android {
    namespace = "uz.toshmatov.currency"
    compileSdk = 36

    defaultConfig {
        applicationId = "uz.toshmatov.currency"
        minSdk = 23
        targetSdk = 36
        versionCode = 13
        versionName = "1.0.3"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }

        ndk {
            abiFilters.add("arm64-v8a")
            abiFilters.add("armeabi-v7a")
        }
    }
    signingConfigs {
        create("release") {
            storeFile = file(project.properties["RELEASE_STORE_FILE"] as String)
            storePassword = project.properties["RELEASE_STORE_PASSWORD"] as String
            keyAlias = project.properties["RELEASE_KEY_ALIAS"] as String
            keyPassword = project.properties["RELEASE_KEY_PASSWORD"] as String
        }
    }


    buildTypes {
        release {
            isShrinkResources = true
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            signingConfig = signingConfigs.getByName("release")

            ndk {
                abiFilters.add("arm64-v8a")
                abiFilters.add("armeabi-v7a")
                debugSymbolLevel = "SYMBOL_TABLE"
            }
        }

        debug {
            applicationIdSuffix = ".debug"
            isDebuggable = true

            isMinifyEnabled = false
            isShrinkResources = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )

            signingConfig = signingConfigs.getByName("debug")

            ndk {
                abiFilters.add("arm64-v8a")
                abiFilters.add("armeabi-v7a")
            }
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }
    packaging {
        // AGP 8.3.x + Play 16 KB page size requirement:
        // package .so as compressed to avoid 4K zip-alignment issue in bundles.
        jniLibs {
            useLegacyPackaging = true
        }
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    bundle {
        language {
            enableSplit = false
        }
    }
    ktlint {
        version = "12.1.0"
        android = true
        ignoreFailures = false
    }
    hilt {
        enableAggregatingTask = true
    }

    ndkVersion = "26.1.10909125"
}

dependencies {

    // androidx core
    implementation(libs.androidx.core.ktx)

    // androidx lifecycle
    implementation(libs.androidx.lifecycle.viewModelCompose)
    implementation(libs.androidx.lifecycle.runtimeCompose)
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
    implementation(libs.androidx.hilt.work)
    ksp(libs.hilt.compiler)
    ksp(libs.androidx.hilt.compiler)


    // kotlin coroutines
    implementation(libs.kotlinx.coroutines.core)

    // kotlin collection
    implementation(libs.kotlinx.collections.immutable)

    // kotlin serialization
    implementation(libs.kotlinx.serialization.json)

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
    implementation(libs.retrofit.core)
    implementation(libs.retrofit.gson)

    // gson
    implementation(libs.gson)

    // room_database
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
    debugImplementation(libs.chucker)
    releaseImplementation(libs.chucker.no.op)

    // system ui
    implementation(libs.accompanist.swiperefresh)
    implementation(libs.accompanist.systemui)

    // lingver
    implementation(libs.lingver)

    // datastore
    implementation(libs.androidx.dataStore.preferences)
    implementation(libs.androidx.dataStore.core)

    // work manager
    implementation(libs.androidx.work.runtime.ktx)

    // admob
    implementation(libs.play.services.ads)
}
