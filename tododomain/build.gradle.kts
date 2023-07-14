plugins {
    id("android-setup")
}

android {
    namespace = ProjectConfig.namespace("data")
}

dependencies {
    implementation(project(":core:di"))
    implementation(project(":core:domain"))
    implementation(project(":auth:domain"))

    implementation(Dependencies.Dagger.dependency)
    kapt(Dependencies.Dagger.compiler)
}