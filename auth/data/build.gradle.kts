plugins {
    id("android-setup")
}

android {
    namespace = ProjectConfig.namespace("auth")

    buildFeatures {
        buildConfig = true
    }
}

dependencies {
    implementation(project(":core:di"))
    implementation(project(":auth:domain"))
    implementation(Dependencies.Other.datastore)
}