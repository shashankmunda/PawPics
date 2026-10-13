import org.gradle.kotlin.dsl.dependencies
import java.io.FileInputStream
import java.util.Properties

plugins {
    id("com.android.application")
    id("dagger.hilt.android.plugin")
    id("com.google.gms.google-services")
    id("com.google.firebase.crashlytics")
    id("com.google.devtools.ksp")
    id("org.jetbrains.kotlin.plugin.compose")
    id("org.jetbrains.kotlin.plugin.serialization")
}

val properties = Properties()
properties.load(FileInputStream(project.rootProject.file("local.properties")))
android {
    signingConfigs {
    }

    buildFeatures{
        buildConfig = true
    }
    compileSdkVersion(36)

    defaultConfig {
        applicationId = "com.shashankmunda.pawpics"
        minSdk  = 23
        targetSdk = 36
        versionCode = 23
        versionName = "1.22"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        buildConfigField("String", "CAT_API_KEY", "\"${properties.getProperty("CAT_API_KEY")}\"")
        signingConfig = signingConfigs.getByName("debug")
        proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"))
    }

    buildTypes {
        getByName("debug") {
            isMinifyEnabled = false
            isShrinkResources = false
            manifestPlaceholders["firebaseDisableMonitoring"] = true
        }
        getByName("release") {
            isMinifyEnabled = true
            isShrinkResources = true
            manifestPlaceholders["firebaseDisableMonitoring"] = false
            proguardFiles (
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    testOptions{
        unitTests{
            isIncludeAndroidResources = true
        }
    }

    namespace = "com.shashankmunda.pawpics"
    sourceSets.getByName("main") {
      assets {
          directories+="src/main/assets"
      }
    }
  }

dependencies {


    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)

    //Navigation component
    implementation(libs.navigation.compose)
    implementation (libs.hilt.navigation.compose)

    //Networking
    implementation (libs.retrofit)
    implementation (libs.retrofit.converter.moshi)

    //Lifecycle-components
    implementation (libs.androidx.activity)
    implementation (libs.lifecycle.runtime.ktx)
    implementation (libs.lifecycle.runtime.compose)
    implementation(libs.androidx.material)

  //Coroutines
    implementation(libs.kotlin.coroutines.android)

    //Room DB
    implementation(libs.room.db)
    ksp(libs.room.compiler)

    //DI
    implementation (libs.dagger.hilt.android)
    ksp (libs.dagger.hilt.android.compiler)

    //Image loading
    implementation(platform(libs.coil.bom))
    implementation(libs.coil.compose)
    implementation(libs.coil.gif)
    implementation(libs.coil.network.okhttp)

    //Testing
    testImplementation (libs.junit)
    testImplementation (libs.test.core)

    val composeBom = platform(libs.composeBom)
    implementation(composeBom)
    androidTestImplementation(composeBom)
    implementation(libs.compose.material3)
    implementation(libs.compose.runtime.live.data)
    implementation(libs.lifecycle.viewmodel.compose)
    implementation(libs.lifecycle.viewmodel.ktx)
    implementation(libs.compose.material.icons.extended)

    implementation(libs.compose.ui.tooling.preview)
    debugImplementation(libs.compose.ui.tooling)
    implementation(libs.accompanist.permissions)
    implementation(libs.google.play.review.ktx)

    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.crashlytics.ktx)
    implementation(libs.firebase.analytics.ktx)
    implementation(libs.firebase.perf.ktx)

    implementation(libs.install.referrer)
}