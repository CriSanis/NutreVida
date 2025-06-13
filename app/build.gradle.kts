plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.nutrevida.app"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.nutrevida.app"
        minSdk = 21
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        buildConfigField("String", "GEMINI_API_KEY", "\"AIzaSyA7S1-mq1zTQ_cBntRlKaEWKypDNnza380\"")
    }

    buildFeatures {
        buildConfig = true
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
}

dependencies {
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation(libs.cardview)
    implementation(libs.recyclerview)
    implementation(libs.gson)
    implementation(libs.generativeai)
    implementation(libs.guava.android)
    implementation("org.commonmark:commonmark:0.22.0")
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
}