plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("kotlin-parcelize")
    id("kotlin-kapt")
}

android {
    namespace = ProjectConfig.namespace("tobedone")
    compileSdk = ProjectConfig.compileSdk

    defaultConfig {
        applicationId = ProjectConfig.applicationId
        minSdk = ProjectConfig.minSdk
        targetSdk = ProjectConfig.targetSdk
        versionCode = ProjectConfig.versionCode
        versionName = ProjectConfig.versionName

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        manifestPlaceholders["YANDEX_CLIENT_ID"] = "0d0970774e284fa8ba9ff70b6b06479a"

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

    buildFeatures {
        viewBinding = true
    }
    compileOptions {
        sourceCompatibility = ProjectConfig.javaVersion
        targetCompatibility = ProjectConfig.javaVersion
    }
    kotlinOptions {
        jvmTarget = ProjectConfig.jvmTarget
    }
}

dependencies {
    implementation(project(":resources"))

    implementation(project(":core:di"))
    implementation(project(":core:data"))
    implementation(project(":core:domain"))

    implementation(project(":auth:ui"))
    implementation(project(":auth:domain"))
    implementation(project(":auth:data"))

    implementation(project(":tododata"))
    implementation(project(":tododomain"))

    implementation(project(":todolist:ui"))
    implementation(project(":todolist:domain"))

    implementation(project(":todorefactor:ui"))
    implementation(project(":todorefactor:domain"))

    implementation(project(":work"))

    implementation(Dependencies.Android.constraintLayout)
    implementation(Dependencies.Android.swiperefreshLayout)
    implementation(Dependencies.Android.coreKtx)
    implementation(Dependencies.Android.appCompat)
    implementation(Dependencies.Android.fragments)
    implementation(Dependencies.Other.workManager)
    implementation(Dependencies.Android.material)
    implementation(Dependencies.Dagger.dependency)

    testImplementation(Dependencies.Testing.junit4)
    androidTestImplementation(Dependencies.Testing.junitAndroidExt)
    androidTestImplementation(Dependencies.Testing.espressoCore)

    kapt(Dependencies.Dagger.compiler)

}

kapt { correctErrorTypes = true }