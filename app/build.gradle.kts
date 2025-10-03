import java.io.FileInputStream
import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.navigation.safe.args)
    alias(libs.plugins.kotlin.ksp)
    alias(libs.plugins.hilt.android)
    alias(libs.plugins.google.services)
    alias(libs.plugins.firebase.crashlytics)
    id("kotlin-parcelize")
}

val localPropertiesFile = rootProject.file("local.properties")
val properties = Properties()
properties.load(FileInputStream(localPropertiesFile))

android {
    namespace = "com.example.objectdetection"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.objectdetection"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        buildConfigField("String", "GOOGLE_SHEETS_API_KEY", "\"${project.findProperty("GOOGLE_SHEETS_API_KEY")}\"")

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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        dataBinding = true
        buildConfig = true
    }

}

dependencies {
    implementation(project(":presentation"))
    implementation(project(":data"))
    implementation(project(":model"))

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.material)
    implementation(libs.androidx.constraintlayout)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)

    implementation(libs.androidx.activity.ktx)
    implementation(libs.androidx.fragment.ktx)

    implementation(libs.google.mlkit.face.detection)
    implementation(libs.play.services.mlkit.face.detection)

    implementation(libs.tensorflow.lite.gpu.delegate.plugin)
    implementation(libs.tensorflow.lite.task.vision)
    implementation(libs.tensorflow.lite.gpu)
    implementation(libs.tensorflow.lite.support)
    implementation(libs.tensorflow.lite.metadata)
    implementation(libs.tensorflow.lite.gpu.old)

    implementation(libs.hilt.android)
    ksp(libs.hilt.android.compiler)


    implementation(libs.material.calendarview)

    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.analytics)
    implementation(libs.firebase.crashlytics)
    implementation(libs.firebase.firestore)
    implementation(libs.firebase.auth)

    implementation(libs.retrofit)
    implementation(libs.retrofit.converter.gson)

    implementation(libs.lottie)

    implementation(libs.androidx.camera.core)
    implementation(libs.androidx.camera.camera2)
    implementation(libs.androidx.camera.lifecycle)
    implementation(libs.androidx.camera.view)
}