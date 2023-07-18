plugins {
    id("android-setup")
}

android {
    namespace = ProjectConfig.namespace("todo.notifications.domain")
}

dependencies {
    implementation(project(":todo:domain"))
}