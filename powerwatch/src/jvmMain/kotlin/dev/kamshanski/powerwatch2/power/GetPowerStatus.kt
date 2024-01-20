package dev.kamshanski.powerwatch2.power

import dev.kamshanski.powerwatch2.Platform
import dev.kamshanski.powerwatch2.load.NativeUtils.loadLibraryFromJar
import dev.kamshanski.powerwatch2.power.GetPowerStatus.Companion.InitState.InitResult

class GetPowerStatus {

	companion object {

		sealed interface InitState {
			data object None : InitState
			sealed interface InitResult : InitState {
				data object Success : InitResult
				data class Failure(val ex: Throwable) : InitResult
			}
		}

		private var initializationResult: InitState = InitState.None

		fun initialize(): InitResult {
			(initializationResult as? InitResult)
				?.let { return it }

			val result = try {
				val libraryName = when (Platform.current) {
					Platform.WINDOWS      -> TODO()
					Platform.MAC_OS_ARM   -> "/macosArm/libpowerwatch.dylib"
					Platform.MAC_OS_INTEL -> "/macosIntel/libpowerwatch.dylib"
				}
				loadLibraryFromJar(libraryName)
				InitResult.Success
			} catch (ex: Throwable) {
				InitResult.Failure(ex)
			}
			initializationResult = result
			return result
		}
	}

	external fun get(): String
}