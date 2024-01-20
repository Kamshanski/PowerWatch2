plugins {
    alias(libs.plugins.multiplatform)
    alias(libs.plugins.kotlinx.serialization)
    id("convention.publication")
}

group = "dev.kamshanski.powerwatch2"
version = "1.0"

kotlin {
    macosX64("macosIntel") {
        binaries.staticLib {
            baseName = "powerwatch"
        }
    }
    macosArm64("macosArm") {
        binaries.staticLib {
            baseName = "powerwatch"
        }
    }
//    linuxX64 {
//        binaries.staticLib {
//            baseName = "powerwatch"
//        }
//    }
    mingwX64("windows") {
        binaries.staticLib {
            baseName = "powerwatch"
        }
    }

    sourceSets {
        commonMain.dependencies {
            implementation(libs.kotlinx.serialization.json)
            implementation(libs.kermit)
            implementation(libs.multiplatformSettings)
        }

        commonTest.dependencies {
            implementation(kotlin("test"))
        }

    }

    //https://kotlinlang.org/docs/native-objc-interop.html#export-of-kdoc-comments-to-generated-objective-c-headers
    targets.withType<org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget> {
        compilations["main"].compilerOptions.options.freeCompilerArgs.add("-Xexport-kdoc")
    }

}
