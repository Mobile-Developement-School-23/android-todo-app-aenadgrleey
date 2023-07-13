plugins {
    id("android-setup")
}

android {
    namespace = ProjectConfig.namespace("data")
}

dependencies {
    implementation(project(":core:di"))
    implementation(Dependencies.Dagger.dependency)
    implementation(Dependencies.Network.Reftofit.gson)
}