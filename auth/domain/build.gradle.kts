plugins {
    id("android-setup")
}

android {
    namespace = ProjectConfig.namespace("auth.domain")

}

dependencies {
    implementation(project(":core:di"))
}