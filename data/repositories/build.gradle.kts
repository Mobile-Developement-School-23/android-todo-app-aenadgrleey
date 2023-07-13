plugins {
    id("android-setup")
}

android {
    namespace = ProjectConfig.namespace("data")
}
dependencies {
    implementation(project(":core:data"))
    implementation(project(":core:di"))
    implementation(project(":data:datasources"))
    implementation(project(":data:datasources:local"))
    implementation(project(":data:datasources:remote"))
}