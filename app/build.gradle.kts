plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.kotlin.kapt)
    alias(libs.plugins.hilt.plugin)
}

android {
    namespace = "com.vanard.vshop"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.vanard.vshop"
        minSdk = 24
        targetSdk = 35
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
        isCoreLibraryDesugaringEnabled = true
    }
    kotlinOptions {
        jvmTarget = "11"
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

    implementation(libs.bundles.room)

    implementation(project(":core:ui"))
    implementation(project(":core:common"))
    implementation(project(":core:resources"))
    implementation(project(":data"))
    implementation(project(":domain"))
    implementation(project(":feature"))
}
