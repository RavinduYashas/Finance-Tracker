plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    id("org.jetbrains.kotlin.kapt")
//    id("org.jetbrains.kotlin.kapt") // ✅ Add this line
}

android {
    namespace = "com.example.financetrackerex3"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.financetrackerex3"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
    }
    kotlinOptions {
        jvmTarget = "11"
    }

}




dependencies {

    // AndroidX & UI
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)

    // Room
    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx)
    kapt(libs.androidx.room.compiler)

    // Room
//    implementation(libs.room.runtime)   // Room runtime
//    implementation(libs.room.ktx)       // Room Kotlin extensions
//    kapt(libs.room.compiler)            // Room annotation processor

    // Other libraries
    implementation("com.google.code.gson:gson:2.10.1")
    implementation("com.github.PhilJay:MPAndroidChart:v3.1.0")

    // Testing
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}

//
//dependencies {
//
//    implementation(libs.androidx.core.ktx)
//    implementation(libs.androidx.appcompat)
//    implementation(libs.material)
//    implementation(libs.androidx.activity)
//    implementation(libs.androidx.constraintlayout)
//
//    implementation(libs.room.runtime)
//    implementation(libs.room.ktx)
//    kapt(libs.room.compiler)
//
//    implementation("com.google.code.gson:gson:2.10.1")
//    implementation("com.github.PhilJay:MPAndroidChart:v3.1.0")
//    implementation ("com.google.android.material:material:1.12.0")
//
//    implementation ("androidx.room:room-runtime:2.6.1")
//    implementation(libs.androidx.room.ktx)
//    kapt ("androidx.room:room-compiler:2.6.1")
//
//    testImplementation(libs.junit)
//    androidTestImplementation(libs.androidx.junit)
//    androidTestImplementation(libs.androidx.espresso.core)
//}