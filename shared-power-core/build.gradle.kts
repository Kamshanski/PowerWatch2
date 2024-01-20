plugins {
	alias(libs.plugins.multiplatform)
	alias(libs.plugins.kotlinx.serialization)
	id("convention.publication")
}

group = "dev.kamshanski.powerwatch2"
version = "1.0"

kotlin {
	jvm()

	val macosIntel = macosX64("macosIntel") {
		binaries.sharedLib {
			baseName = "powerwatch"
		}
	}
	val macosArm = macosArm64("macosArm") {
		binaries.sharedLib {
			baseName = "powerwatch"
		}
	}
	val windows = mingwX64("windows") {
		binaries.sharedLib {
			baseName = "powerwatch"
		}
	}

	sourceSets {
		all {
			languageSettings {
				optIn("kotlin.experimental.ExperimentalNativeApi")
//                optIn("org.jetbrains.compose.resources.ExperimentalResourceApi")
			}
		}
		commonMain.dependencies {
			implementation(libs.kotlinx.serialization.json)
//			implementation(libs.kermit)
//			implementation(libs.multiplatformSettings)
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
