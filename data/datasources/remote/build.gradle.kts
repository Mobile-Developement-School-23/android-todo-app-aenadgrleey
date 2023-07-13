plugins {
    id("android-setup")
}

android {
    namespace = ProjectConfig.namespace("data")
}

dependencies {
    implementation(project(":core:data"))
    implementation(project(":core:di"))
    implementation(Dependencies.Dagger.dependency)
    implementation(Dependencies.Network.Reftofit.retrofit)
    implementation(Dependencies.Network.Reftofit.gson)
}