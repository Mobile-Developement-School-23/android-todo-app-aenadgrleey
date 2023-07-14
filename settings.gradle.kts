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
include(":data:datasources")
include(":data:repositories")
include(":data:datasources:local")
include(":data:datasources:remote")
include(":resources")
include(":myapplication")
include(":auth")
include(":auth:data")
include(":auth:ui")
include(":auth:domain")
include(":todolist:ui")
include(":todolist:domain")
include(":core:domain")
include(":todorefactor:ui")
include(":todorefactor:domain")
include(":work")
include(":tododomain")
include(":tododata")
include(":core:ui")
