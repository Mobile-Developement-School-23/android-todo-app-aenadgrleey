plugins {
    id("android-setup")
}

android {
    namespace = ProjectConfig.namespace("todo.domain")
}

dependencies {
    implementation(project(":core:di"))
    implementation(project(":core:domain"))
    implementation(project(":auth:domain"))

    implementation(Dependencies.Dagger.dependency)
    kapt(Dependencies.Dagger.compiler)
}