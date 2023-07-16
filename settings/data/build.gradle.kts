plugins {
    id("android-setup")
}

android {
    namespace = ProjectConfig.namespace("settings.data")
}

dependencies {
    implementation(project(":core:di"))
    implementation(project(":settings:domain"))
    implementation(Dependencies.Other.datastore)
}