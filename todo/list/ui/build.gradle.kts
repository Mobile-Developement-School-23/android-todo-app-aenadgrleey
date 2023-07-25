plugins {
    id("android-setup")
}

android {
    namespace = ProjectConfig.namespace("todo.list.ui")
    buildFeatures {
        viewBinding = true
    }
}

dependencies {
    implementation(project(":resources"))
    implementation(project(":core:di"))
    implementation(project(":core:domain"))
    implementation(project(":core:ui"))
    implementation(project(":todo:domain"))
    implementation(project(":todo:list:domain"))

    implementation(Dependencies.Android.constraintLayout)
    implementation(Dependencies.Android.swiperefreshLayout)
    implementation(Dependencies.Android.coreKtx)
    implementation(Dependencies.Android.appCompat)
    implementation(Dependencies.Android.fragments)
    implementation(Dependencies.Android.material)
}