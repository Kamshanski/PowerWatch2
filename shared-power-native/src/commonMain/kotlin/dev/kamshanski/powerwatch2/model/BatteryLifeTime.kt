package dev.kamshanski.powerwatch2.model

// https://docs.microsoft.com/en-us/windows/win32/api/winbase/ns-winbase-system_power_status
/**
 * The number of seconds of battery life remaining,
 *  or –1 if remaining seconds are unknown or if the device is connected to AC power.
 */
sealed interface BatteryLifeTime {

	data class Duration(val value: Long, val unit: TimeUnit) : BatteryLifeTime

	data object Calculating : BatteryLifeTime

	data class Undefined(val message: String) : BatteryLifeTime

	//________________________
	enum class TimeUnit {
		SECONDS,
		MINUTES,
	}
}