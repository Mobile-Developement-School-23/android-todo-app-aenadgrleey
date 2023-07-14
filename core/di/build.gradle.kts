plugins {
    id("android-setup")
}

android {
    namespace = ProjectConfig.namespace("di")
}

dependencies {
    implementation(Dependencies.Android.coreKtx)
    implementation(Dependencies.Android.lifecycle)
}