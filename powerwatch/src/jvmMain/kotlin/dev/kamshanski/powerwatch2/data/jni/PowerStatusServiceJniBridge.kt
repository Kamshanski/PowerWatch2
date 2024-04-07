package dev.kamshanski.powerwatch2.data.jni

import dev.kamshanski.powerwatch2.Platform
import dev.kamshanski.powerwatch2.data.jni.NativeUtils.loadLibraryFromJar
import dev.kamshanski.powerwatch2.data.jni.PowerStatusServiceJniBridge.Companion.InitState.InitResult

// TODO try add interface for this
class PowerStatusServiceJniBridge {

	companion object {

		sealed interface InitState {
			data object None : InitState
			sealed interface InitResult : InitState {
				data object Success : InitResult
				data class Failure(val ex: Throwable) : InitResult
			}
		}

		private var initializationResult: InitState = InitState.None
	}

	/** @throws RuntimeException при невозможности загрузить библиотеку в JVM, провале определения платформы, невозможности прочитать библиотеку из Jar */
	fun initBridge(): InitResult {
		(initializationResult as? InitResult)
			?.let { return it }

		val libraryName = when (Platform.current) {
			Platform.WINDOWS      -> "/macosIntel/libpowerwatch.dll"
			Platform.MAC_OS_ARM   -> "/macosArm/libpowerwatch.dylib"
			Platform.MAC_OS_INTEL -> "/macosIntel/libpowerwatch.dylib"
		}

		val result = try {
			loadLibraryFromJar(libraryName)
			InitResult.Success
		} catch (ex: Throwable) {
			throw RuntimeException("Failed to load library $libraryName on ${Platform.current}", ex)
		}
		initializationResult = result
		return result
	}

	external fun getPowerStateJsonInternal(): String

	fun getPowerStateJson(): String {
		check(initializationResult is InitResult.Success) { "Uninitialized jni bridge. Run initBridge before getting power status." }

		return getPowerStateJsonInternal()
	}
}