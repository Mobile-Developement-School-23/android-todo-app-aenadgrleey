plugins {
    id("android-setup")
}

android {
    namespace = ProjectConfig.namespace("core.ui")
}

dependencies {
    implementation(project(":core:domain"))
}