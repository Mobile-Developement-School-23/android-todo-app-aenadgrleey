plugins {
    id("android-setup")
}

android {
    namespace = ProjectConfig.namespace("todolist.ui")
    buildFeatures {
        viewBinding = true
    }
}

dependencies {
    implementation(project(":resources"))
    implementation(project(":core:domain"))
    implementation(project(":core:di"))
    implementation(project(":tododomain"))
    implementation(project(":todolist:domain"))

    implementation(Dependencies.Android.constraintLayout)
    implementation(Dependencies.Android.swiperefreshLayout)
    implementation(Dependencies.Android.coreKtx)
    implementation(Dependencies.Android.appCompat)
    implementation(Dependencies.Android.fragments)
    implementation(Dependencies.Android.material)
}