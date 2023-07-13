plugins {
    id("android-setup")
}

android {
    namespace = ProjectConfig.namespace("data")
}

dependencies {
    implementation(project(":core:data"))
    implementation(project(":core:di"))
    api(project(":data:datasources:local"))
    api(project(":data:datasources:remote"))

    implementation(Dependencies.Dagger.dependency)
    kapt(Dependencies.Dagger.compiler)
}
