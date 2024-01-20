package dev.kamshanski.powerwatch2.power

import kotlinx.serialization.Serializable

// https://docs.microsoft.com/en-us/windows/win32/api/winbase/ns-winbase-system_power_status
/**
 * The percentage of full battery charge remaining.
 * This member can be a value in the range 0 to 100, or 255 if status is unknown.
 */
@Serializable
sealed interface BatteryCharge {

    @Serializable
    data class Percent(val value: Int) : BatteryCharge {

        init { require(value in 0..100) }

        override fun toString(): String = "BatteryCharge($value%)"
    }

    @Serializable
    data class Unknown(val message: String?) : BatteryCharge
}