pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    //TODO make it RepositoriesMode.FAIL_ON_PROJECT_REPOS
    repositoriesMode.set(RepositoriesMode.PREFER_PROJECT)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "ToBeDone"
include(":app")
include(":todolist")
include(":todorefactor")
include(":core")
include(":core:di")
include(":core:data")
include(":resources")
include(":myapplication")
include(":auth")
include(":auth:data")
include(":auth:ui")
include(":auth:domain")
include(":todo:list:ui")
include(":todo:list:domain")
include(":todo:refactor:ui")
include(":todo:refactor:domain")
include(":todo:work")
include(":todo:domain")
include(":todo:data")
include(":settings")
include(":settings:data")
include(":settings:domain")
include(":settings:ui")
include(":todo:notifications")
include(":todo:notifications:domain")
include(":todo:notifications:ui")
include(":todo")
