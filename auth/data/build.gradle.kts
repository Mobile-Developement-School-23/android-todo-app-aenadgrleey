plugins {
    id("android-setup")
}

android {
    namespace = ProjectConfig.namespace("auth")
}

dependencies {
    implementation(Dependencies.Other.datastore)
}