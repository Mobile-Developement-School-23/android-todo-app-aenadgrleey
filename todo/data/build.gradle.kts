plugins {
    id("android-setup")
}

android {
    namespace = ProjectConfig.namespace("todo.data")
    defaultConfig {
        kapt {
            arguments {
                arg("room.schemaLocation", "$projectDir/schemas")
            }
        }
    }
}

dependencies {
    implementation(project(":resources"))
    implementation(project(":core:data"))
    implementation(project(":core:di"))
    implementation(project(":todo:domain"))
    implementation(project(":auth:domain"))
    implementation(project(":todo:notifications:domain"))

    implementation(Dependencies.Room.roomKtx)
    implementation(Dependencies.Network.Reftofit.retrofit)
    implementation(Dependencies.Network.Reftofit.gson)
    kapt(Dependencies.Room.roomCompiler)
}