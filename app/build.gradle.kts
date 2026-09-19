// Gradle-конфигурация модуля приложения QuizFlags.
plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)   // ← обязательно при Kotlin 2.0+
    alias(libs.plugins.ksp)              // ← если используешь Room
}

android {
    namespace = "com.example.quizflags"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.example.quizflags"
        minSdk = 26
        targetSdk = 36
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
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures {
        compose = true
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(libs.androidx.activity.compose)

    // Jetpack Compose (Material 3, версии берём из BOM).
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui.tooling.preview)
    debugImplementation(libs.androidx.ui.tooling)
    implementation(libs.androidx.compose.material3)

    // Навигация.
    implementation(libs.androidx.navigation.compose)

    // Room (runtime + KTX), компилятор через KSP.
    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx)
    ksp(libs.androidx.room.compiler)

    // Загрузка изображений (флаги PNG).
    implementation(libs.coil.compose)

    // Корутины.
    implementation(libs.kotlinx.coroutines.android)

    // DataStore Preferences.
    implementation(libs.androidx.datastore.preferences)
}