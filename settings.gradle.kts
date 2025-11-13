pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal() // Essential for finding plugins like KSP
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "movDVD"
include(":app")