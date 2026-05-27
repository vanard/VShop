plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.kotlin.kapt)
    alias(libs.plugins.hilt.plugin)
    alias(libs.plugins.google.gms.google.services)
}

android {
    namespace = "com.vanard.vshop"
    compileSdk = rootProject.extra["compileSdk"] as Int

    defaultConfig {
        applicationId = "com.vanard.vshop"
        minSdk = rootProject.extra["minSdk"] as Int
        targetSdk = rootProject.extra["targetSdk"] as Int
        versionCode = 1
        versionName = "1.0"

//        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        testInstrumentationRunner = "com.vanard.vshop.HiltRunner"
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
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
        isCoreLibraryDesugaringEnabled = true
    }
    buildFeatures {
        compose = true
    }

    testOptions {
        unitTests.isIncludeAndroidResources = true
    }
}

dependencies {

    implementation(libs.androidx.ui.test.junit4.android)
    implementation(libs.androidx.navigation.testing)
    implementation(libs.androidx.runner)

//    testImplementation(libs.hilt.testing)
//    kaptTest(libs.hilt.android.compiler)
    androidTestImplementation(libs.hilt.testing)
    kaptAndroidTest(libs.hilt.android.compiler)

//    testImplementation(libs.robolectric)

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    coreLibraryDesugaring(libs.desugar.jdk.libs)

    //splash api
    implementation(libs.splashscreen)

    //compose navigation
    implementation(libs.androidx.navigation)

    //lifecycle compose
    implementation(libs.lifecycle.compose)

    //hilt
    implementation(libs.hilt.android)
    kapt(libs.hilt.compiler)

    implementation(libs.hilt.navigation)

    // Firebase BoM and Auth
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.auth)
    // Security Crypto for encrypted storage
    implementation(libs.androidx.security.crypto)
    // DataStore Preferences
    implementation(libs.datastore)
    // Paging 3
    implementation(libs.paging.runtime)
    implementation(libs.paging.compose)

    implementation(libs.bundles.room)

    implementation(project(":core:ui"))
    implementation(project(":core:common"))
    implementation(project(":core:resources"))
    implementation(project(":data"))
    implementation(project(":domain"))
    implementation(project(":feature"))
}
