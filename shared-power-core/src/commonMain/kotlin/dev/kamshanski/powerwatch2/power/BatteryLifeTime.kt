package dev.kamshanski.powerwatch2.power

import kotlinx.serialization.Serializable

// https://docs.microsoft.com/en-us/windows/win32/api/winbase/ns-winbase-system_power_status
/**
 * The number of seconds of battery life remaining,
 *  or –1 if remaining seconds are unknown or if the device is connected to AC power.
 */
@Serializable
sealed interface BatteryLifeTime {

	@Serializable
	data class Duration(val value: Long, val unit: TimeUnit) : BatteryLifeTime

	@Serializable
	data object Calculating : BatteryLifeTime

	@Serializable
	data class Undefined(val message: String) : BatteryLifeTime

	//________________________
	@Serializable
	enum class TimeUnit {
		SECONDS,
		MINUTES,
	}
}