pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {

    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "ToBeDone"

include(":app")

include(":resources")

include(":core:di")
include(":core:data")

include(":auth:data")
include(":auth:ui")
include(":auth:domain")

include(":settings:data")
include(":settings:domain")
include(":settings:ui")

include(":todo:data")
include(":todo:domain")

include(":todo:list:domain")
include(":todo:list:ui")

include(":todo:refactor:domain")
include(":todo:refactor:ui")

include(":todo:notifications:domain")
include(":todo:notifications:ui")

include(":todo:work")
include(":core:ui")
