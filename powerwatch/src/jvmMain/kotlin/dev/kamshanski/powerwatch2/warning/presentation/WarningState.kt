package dev.kamshanski.powerwatch2.warning.presentation

import dev.kamshanski.powerwatch2.power.BatteryCharge
import dev.kamshanski.powerwatch2.power.BatteryLifeTime

sealed interface WarningState {

	data object Hiding : WarningState

	data class Warning(val charge: BatteryCharge.Percent, val lifeTime: BatteryLifeTime) : WarningState

	data class Failure(val message: String) : WarningState

	data object Initial : WarningState
}
