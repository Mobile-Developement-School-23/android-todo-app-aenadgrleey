plugins {
    id("android-setup")
}

android {
    namespace = ProjectConfig.namespace("data")
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
    implementation(project(":core:domain"))
    implementation(project(":core:di"))
    implementation(project(":tododomain"))
    implementation(project(":auth:domain"))

    implementation(Dependencies.Dagger.dependency)
    implementation(Dependencies.Room.roomKtx)
    implementation(Dependencies.Network.Reftofit.retrofit)
    implementation(Dependencies.Network.Reftofit.gson)
    kapt(Dependencies.Room.roomCompiler)
    kapt(Dependencies.Dagger.compiler)
}