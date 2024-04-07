import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    alias(libs.plugins.multiplatform)
    alias(libs.plugins.compose)
    alias(libs.plugins.kotlinx.serialization)
    id("convention.publication")
}

group = "dev.kamshanski.powerwatch2"
version = "1.0"

kotlin {
    jvm {
        withJava()
    }

    sourceSets {
        commonMain.dependencies {
            implementation(libs.kotlinx.serialization.json)
            implementation(project(":shared-power-core"))
//            implementation(libs.kermit)
            implementation(libs.multiplatformSettings)
            implementation(compose.runtime)
            implementation(compose.material3)
            implementation(compose.desktop.currentOs)
            implementation(libs.coroutines.swing)
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
compose.desktop {
    application {
        mainClass = "dev.kamshanski.powerwatch2.MainKt"
        javaHome = "/Library/Java/JavaVirtualMachines/amazon-corretto-17.jdk/Contents/Home"

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Deb)
            packageName = "dev.kamshanski.powerwatch2"
            packageVersion = "1.0.0"
        }
    }
}
