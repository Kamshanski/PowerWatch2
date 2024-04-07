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
//    linuxX64 {
//        binaries.staticLib {
//            baseName = "powerwatch"
//        }
//    }
	val windows = mingwX64("windows") {
		binaries.sharedLib {
			baseName = "powerwatch"
		}
	}
	listOf(
		macosArm,
		macosIntel,
		windows,
	).forEach {
		it.compilations.getByName("main") {
			cinterops {
				val jni by creating {
					val javaHome = File(System.getenv("JAVA_HOME") ?: System.getProperty("java.home"))
					val javaHomeInclude = File(javaHome, "include")
					val javaHomeIncludeDarwin = File(javaHomeInclude, "darwin")

					defFile(project.file("src/nativeInterop/cinterop/jni.def"))
					packageName = "jni"
					includeDirs(
						javaHomeInclude.also { println("javaHomeInclude '$it'") },
						javaHomeIncludeDarwin.also { println("javaHomeIncludeDarwin '$it'") },
					)
				}
			}
		}
//		cinterops.create("jni") {
//			// JDK is required here, JRE is not enough
//			val javaHome = File(System.getenv("JAVA_HOME") ?: System.getProperty("java.home"))
//			require(javaHome.exists())
//			packageName = "generated.jni"
//			includeDirs(
//				Callable { File(javaHome, "include").also { require(it.exists()) } },
////				Callable { File(javaHome, "include/win32") },
//				Callable { File(javaHome, "include/darwin").also { require(it.exists()) } },
//			)
//		}
	}

	sourceSets {
		all {
			languageSettings {
				optIn("kotlin.experimental.ExperimentalNativeApi")
				optIn("kotlinx.cinterop.ExperimentalForeignApi")
//                optIn("org.jetbrains.compose.resources.ExperimentalResourceApi")
			}
		}
		commonMain.dependencies {
			implementation(libs.kotlinx.serialization.json)
//			implementation(libs.kermit)
//			implementation(libs.multiplatformSettings)
		}
		nativeMain.dependencies {
			implementation(project(":shared-power-core"))
			implementation(project(":shared-power-native"))
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
