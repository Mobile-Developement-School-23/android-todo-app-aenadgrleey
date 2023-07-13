plugins {
    id("android-setup")
    id("compose-setup")
}

android {
    namespace = ProjectConfig.namespace("auth")
}

dependencies {
    implementation(Dependencies.Android.fragments)
}