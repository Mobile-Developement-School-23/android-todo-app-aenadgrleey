plugins {
    id("android-setup")
}

android {
    namespace = ProjectConfig.namespace("work")
}

dependencies {
    implementation(project(":core:di"))
    implementation(project(":core:data"))
    implementation(project(":core:domain"))
    implementation(project(":tododomain"))
    implementation(Dependencies.Other.workManager)
}