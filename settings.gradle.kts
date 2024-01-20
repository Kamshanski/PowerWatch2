rootProject.name = "PowerWatch2"
include(":powerwatch")
include(":shared-power-native")
include(":shared-power-jni")
includeBuild("convention-plugins")

pluginManagement {
    repositories {
        google()
        gradlePluginPortal()
        mavenCentral()
    }
}

dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
    }
}
