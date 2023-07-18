plugins {
    id("android-setup")
}

android {
    namespace = ProjectConfig.namespace("todonotify")
}

dependencies {

    implementation(project(":resources"))
    implementation(project(":core:di"))
    implementation(project(":core:domain"))
    implementation(project(":tododomain"))
    implementation(project(":todonotify:domain"))

    implementation(Dependencies.Android.coreKtx)
    implementation(Dependencies.Dagger.android)
    kapt(Dependencies.Dagger.androidCompiler)

}