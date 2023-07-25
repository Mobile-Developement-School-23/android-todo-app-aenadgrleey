plugins {
    id("android-setup")
}

android {
    namespace = ProjectConfig.namespace("todo.notifications")
}

dependencies {

    implementation(project(":resources"))
    implementation(project(":core:di"))
    implementation(project(":todo:domain"))
    implementation(project(":todo:notifications:domain"))

    implementation(Dependencies.Android.coreKtx)
}