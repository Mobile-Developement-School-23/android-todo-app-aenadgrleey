plugins {
    id("android-setup")
    id("compose-setup")
}

android {
    namespace = ProjectConfig.namespace("todorefactor.ui")
}

dependencies {
    implementation(project(":resources"))
    implementation(project(":core:domain"))
    implementation(project(":core:di"))
    implementation(project(":tododomain"))
    implementation(project(":todorefactor:domain"))

    implementation(Dependencies.Android.constraintLayout)
    implementation(Dependencies.Android.swiperefreshLayout)
    implementation(Dependencies.Android.coreKtx)
    implementation(Dependencies.Android.appCompat)
    implementation(Dependencies.Android.fragments)
    implementation(Dependencies.Android.material)
}