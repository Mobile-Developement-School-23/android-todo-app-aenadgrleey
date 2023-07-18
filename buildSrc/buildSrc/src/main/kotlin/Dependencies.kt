/**
 * Provides all used in project dependencies
 */
object Dependencies {
    object Kotlin {
        const val ver = "1.8.21"

        const val gradlePlugin = "org.jetbrains.kotlin:kotlin-gradle-plugin:$ver"

        const val coroutines = "org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.2"
    }

    object Android {
        private const val gradlePluginVersion = "8.0.2"
        private const val materialVersion = "1.9.0"
        private const val activityComposeVersion = "1.7.2"
        private const val coreKtxVersion = "1.10.1"
        private const val lifecycleVersion = "2.6.1"
        private const val appCompatVersion = "1.6.1"
        private const val constraintLayoutVersion = "2.1.4"
        private const val swiperefrechLayout = "1.1.0"
        private const val fragmentsVersion = "1.6.0"

        const val gradlePlugin = "com.android.tools.build:gradle:$gradlePluginVersion"
        const val activityCompose = "androidx.activity:activity-compose:$activityComposeVersion"
        const val coreKtx = "androidx.core:core-ktx:$coreKtxVersion"
        const val appCompat = "androidx.appcompat:appcompat:$appCompatVersion"
        const val material = "com.google.android.material:material:$materialVersion"
        const val lifecycle = "androidx.lifecycle:lifecycle-viewmodel-ktx:$lifecycleVersion"

        const val constraintLayout = "androidx.constraintlayout:constraintlayout:$constraintLayoutVersion"
        const val swiperefreshLayout = "androidx.swiperefreshlayout:swiperefreshlayout:$swiperefrechLayout"
        const val fragments = "androidx.fragment:fragment-ktx:$fragmentsVersion"


    }

    object Dagger {
        private const val version = "2.46.1"

        const val dependency = "com.google.dagger:dagger:$version"
        const val compiler = "com.google.dagger:dagger-compiler:$version" //kapt
        const val android = "com.google.dagger:dagger-android:$version"
        const val androidCompiler = "com.google.dagger:dagger-android-processor:$version"
    }

    object Compose {
        private const val version = "1.4.3"
        private const val material3Version = "1.1.1"
        private const val navigationVersion = "2.6.0"
        private const val lifecycleRuntimeVersion = "2.6.1"
        private const val materialThemeAdapterVersion = "0.31.1-alpha"

        const val ui = "androidx.compose.ui:ui:$version"
        const val tooling = "androidx.compose.ui:ui-tooling:$version"
        const val material3 = "androidx.compose.material3:material3:$material3Version"
        const val navigation = "androidx.navigation:navigation-compose:$navigationVersion"
        const val lifecycleRuntime = "androidx.lifecycle:lifecycle-runtime-compose:$lifecycleRuntimeVersion"
        const val materialThemeAdater = "com.google.accompanist:accompanist-themeadapter-material3:$materialThemeAdapterVersion"

    }

    object Room {
        private const val version = "2.5.2"

        const val roomKtx = "androidx.room:room-ktx:$version"
        const val roomCompiler = "androidx.room:room-compiler:$version" //ksp & annotationProcessor
    }

    object Testing {
        const val junit4 = "junit:junit:4.13.2" //testImplementation
        const val junitAndroidExt = "androidx.test.ext:junit:1.1.5" //androidTestImplementation
        const val espressoCore = "androidx.test.espresso:espresso-core:3.5.1" //androidTestImplementation
    }

    object Other {
        private const val yandexAuthSdkVersion = "2.5.1"
        private const val datastoreVersion = "1.0.0"
        private const val workManagerVersion = "2.8.1"

        const val yandexAuthSdk = "com.yandex.android:authsdk:$yandexAuthSdkVersion"
        const val datastore = "androidx.datastore:datastore-preferences:$datastoreVersion"
        const val workManager = "androidx.work:work-runtime-ktx:$workManagerVersion"
    }

    object Network {
        object Reftofit {
            private const val retrofitVersion = "2.9.0"
            const val retrofit = "com.squareup.retrofit2:retrofit:$retrofitVersion"
            const val gson = "com.squareup.retrofit2:converter-gson:$retrofitVersion"
        }
    }
}
