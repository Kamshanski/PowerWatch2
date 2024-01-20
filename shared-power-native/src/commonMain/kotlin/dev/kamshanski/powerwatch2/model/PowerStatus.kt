package dev.kamshanski.powerwatch2.model

sealed interface PowerStatus {

	/** Has battery */
	sealed interface Autonomous : PowerStatus {

		val charge: BatteryCharge

		/** Battery is charging */
		data class Charging(override val charge: BatteryCharge) : Autonomous

		/** Battery is not charging */
		data class InUse(override val charge: BatteryCharge, val lifeTime: BatteryLifeTime) : Autonomous
	}

	/** No battery inside device. Only AC cable */
	data object WireOnly : PowerStatus

	data class NoPowerSourcesFound(val message: String) : PowerStatus
}
