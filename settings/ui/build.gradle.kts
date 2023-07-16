plugins {
    id("android-setup")
    id("compose-setup")
}

android {
    namespace = ProjectConfig.namespace("setting.ui")
}

dependencies {

    implementation(project(":resources"))

    implementation(project(":core:di"))
    implementation(project(":core:data"))
    implementation(project(":core:domain"))

    implementation(project(":settings:domain"))
    implementation(project(":auth:domain"))

    implementation(Dependencies.Android.material)
    implementation(Dependencies.Android.appCompat)
    implementation(Dependencies.Android.fragments)
}