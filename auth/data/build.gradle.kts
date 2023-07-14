plugins {
    id("android-setup")
}

android {
    namespace = ProjectConfig.namespace("auth")
}

dependencies {
    implementation(project(":core:di"))
    implementation(project(":auth:domain"))
    implementation(Dependencies.Other.datastore)
}