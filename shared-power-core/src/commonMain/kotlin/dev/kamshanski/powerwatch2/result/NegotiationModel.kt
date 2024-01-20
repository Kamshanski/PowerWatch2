package dev.kamshanski.powerwatch2.result

import dev.kamshanski.powerwatch2.power.PowerStatus
import kotlinx.serialization.Polymorphic
import kotlinx.serialization.Serializable

/**
 * If [isError] == true, then value is error message. Otherwise, value is payload
 */
@Serializable
sealed interface NegotiationModel {

	@Serializable
	data class Error(val value: String) : NegotiationModel

	@Serializable
	data class PowerStatusResult(@Polymorphic val value: PowerStatus): NegotiationModel

	companion object {

		fun Error(error: Throwable) = Error(
			value = error.stackTraceToString()
		)
	}
}
