package dev.kamshanski.powerwatch2.power

import kotlinx.serialization.Polymorphic
import kotlinx.serialization.Serializable

@Serializable
sealed interface PowerStatus {

	/** Has battery */
	@Serializable
	sealed interface Autonomous : PowerStatus {

		val charge: BatteryCharge

		/** Battery is charging */
		@Serializable
		data class Charging(
			@Polymorphic override val charge: BatteryCharge,
		) : Autonomous

		/** Battery is not charging */
		@Serializable
		data class InUse(
			@Polymorphic override val charge: BatteryCharge,
			@Polymorphic val lifeTime: BatteryLifeTime,
		) : Autonomous
	}

	/** No battery inside device. Only AC cable */
	@Serializable
	data object WireOnly : PowerStatus

	@Serializable
	data class NoPowerSourcesFound(val message: String) : PowerStatus
}
