plugins {
    id("android-setup")
}

android {
    namespace = ProjectConfig.namespace("todo.domain")
}

dependencies {
    implementation(project(":core:di"))
}