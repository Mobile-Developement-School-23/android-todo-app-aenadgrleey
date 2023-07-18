plugins {
    id("android-setup")
    id("compose-setup")
}

android {
    namespace = ProjectConfig.namespace("todo.refactor.ui")
}

dependencies {
    implementation(project(":resources"))
    implementation(project(":core:di"))
    implementation(project(":todo:domain"))
    implementation(project(":todo:refactor:domain"))

    implementation(Dependencies.Android.constraintLayout)
    implementation(Dependencies.Android.swiperefreshLayout)
    implementation(Dependencies.Android.coreKtx)
    implementation(Dependencies.Android.appCompat)
    implementation(Dependencies.Android.fragments)
    implementation(Dependencies.Android.material)
}