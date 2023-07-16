plugins {
    id("android-setup")
}

android {
    namespace = ProjectConfig.namespace("core.di")
}

dependencies {
    implementation(Dependencies.Android.coreKtx)
    implementation(Dependencies.Android.lifecycle)
}