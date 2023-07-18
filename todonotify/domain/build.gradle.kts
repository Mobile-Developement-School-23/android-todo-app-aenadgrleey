plugins {
    id("android-setup")
}

android {
    namespace = ProjectConfig.namespace("todonotify.domain")
}

dependencies {
    implementation(project(":core:domain"))
}