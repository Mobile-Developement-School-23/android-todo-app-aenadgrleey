plugins {
    id("android-setup")
}

android {
    namespace = ProjectConfig.namespace("data")
    kapt {
        arguments {
            arg("room.schemaLocation", "$projectDir/schemas")
        }
    }
}

dependencies {
    implementation(project(":core:data"))
    implementation(project(":core:di"))
    implementation(project(":resources"))
    implementation(Dependencies.Dagger.dependency)
    kapt(Dependencies.Room.roomCompiler)
    implementation(Dependencies.Room.roomKtx)
}