plugins {
    id("android-setup")
}

android {
    namespace = ProjectConfig.namespace("resources")
}
dependencies {
    implementation(Dependencies.Android.material)
}