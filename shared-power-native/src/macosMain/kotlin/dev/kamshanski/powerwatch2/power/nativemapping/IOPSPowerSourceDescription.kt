package dev.kamshanski.powerwatch2.power.nativemapping

import platform.IOKit.kIOPSCurrentCapacityKey
import platform.IOKit.kIOPSIsChargingKey
import platform.IOKit.kIOPSIsPresentKey
import platform.IOKit.kIOPSMaxCapacityKey
import platform.IOKit.kIOPSPowerSourceStateKey
import platform.IOKit.kIOPSTimeToEmptyKey
import platform.IOKit.kIOPSTimeToFullChargeKey
import platform.IOKit.kIOPSTypeKey

/** https://developer.apple.com/documentation/iokit/iopskeys_h/defines
 * Примеры списков:
 * [{Is Present=true, Max Capacity=100, LPM Active=false, Power Source State=AC Power,      Is Charged=true, Current=-405, Battery Provides Time Remaining=true, Name=InternalBattery-0, Is Charging=false, DesignCycleCount=1000, Current Capacity=100, Transport Type=Internal, Hardware Serial Number=D861353A1QRK7LNB0, Type=InternalBattery, BatteryHealthCondition=, BatteryHealth=Good, Optimized Battery Charging Engaged=false, Time to Empty=0 , Time to Full Charge=0, Power Source ID=7340131}]
 * [{Is Present=true, Max Capacity=100, LPM Active=false, Power Source State=Battery Power,                  Current=0,    Battery Provides Time Remaining=true, Name=InternalBattery-0, Is Charging=false, DesignCycleCount=1000, Current Capacity=100, Transport Type=Internal, Hardware Serial Number=D861353A1QRK7LNB0, Type=InternalBattery, BatteryHealthCondition=, BatteryHealth=Good, Optimized Battery Charging Engaged=false, Time to Empty=-1, Time to Full Charge=0, Power Source ID=7340131}]
 * */
@Suppress("ReplaceGetOrSet")
data class IOPSPowerSourceDescription(
	/** kIOPSIsPresentKey key for the current power source's presence.
	 * Providing this key is REQUIRED.
	 * Type CFBoolean - kCFBooleanTrue or kCFBooleanFalse
	 */
	val isPresent: Boolean,

	/** kIOPSPowerSourceStateKey key for the current source of power.
	 * Providing this key is REQUIRED.
	 * Type CFString, value is kIOPSACPowerValue, kIOPSBatteryPowerValue, or kIOPSOffLineValue.
	 */
	val powerSourceState: IOPSPowerSourceState,

	/** kIOPSMaxCapacityKey key for the current power source's maximum or "Full Charge Capacity".
	 * Providing this key is REQUIRED.
	 * Type CFNumber kCFNumberIntType (signed integer)
	 */
	val maxCapacity: Long,

	/** kIOPSTimeToEmptyKey key for the current power source's time remaining until empty.
	 * Only valid if the power source is running off its own power. That's when the kIOPSPowerSourceStateKey has value kIOPSBatteryPowerValue
	 *    and the value of kIOPSIsChargingKey is kCFBooleanFalse.
	 * Providing this key is RECOMMENDED.
	 * Type CFNumber kCFNumberIntType (signed integer), units are minutes.
	 * A value of -1 indicates "Still Calculating the Time", otherwise estimated minutes left on the battery.
	 */
	val timeToEmpty: Long?,

	/** kIOPSTimeToFullChargeKey key for the current power source's time remaining until empty.
	 * Only valid if the value of kIOPSIsChargingKey is kCFBooleanTrue.
	 * Providing this key is RECOMMENDED.
	 * Type CFNumber kCFNumberIntType (signed integer), units are minutes.
	 * A value of -1 indicates "Still Calculating the Time", otherwise estimated minutes until fully charged.
	 */
	val timeToFullCharge: Long?,

	/** kIOPSIsChargingKey key for the current power source's charging state.
	 * NOTE! This field changes slowly. Use powerSourceState to check charging
	 * Providing this key is REQUIRED.
	 * Type CFBoolean - kCFBooleanTrue or kCFBooleanFalse
	 */
	val isCharging: Boolean,

	/** kIOPSCurrentCapacityKey key for the current power source's capacity.
	 * Clients may derive a percentage of power source battery remaining by dividing "Current Capacity" by "Max Capacity".
	 * Providing this key is REQUIRED.
	 * Type CFNumber kCFNumberIntType (signed integer)
	 */
	val currentCapacity: Long,

	/** kIOPSTypeKey key for the current power source's capacity.
	 * Providing this key is REQUIRED.
	 * Type CFStringRef. Valid transport types are kIOPSUPSType or kIOPSInternalBatteryType.
	 */
	val type: IOPSPowerSourceType,
) {

	constructor(map: Map<String, Any?>) : this(
		isPresent = map.require(kIOPSIsPresentKey) as Boolean,
		powerSourceState = (map.require(kIOPSPowerSourceStateKey) as String).toPowerSourceState(),
		maxCapacity = map.require(kIOPSMaxCapacityKey) as Long,
		timeToEmpty = map.get(kIOPSTimeToEmptyKey) as? Long,
		timeToFullCharge = map.get(kIOPSTimeToFullChargeKey) as? Long,
		isCharging = map.require(kIOPSIsChargingKey) as Boolean,
		currentCapacity = map.require(kIOPSCurrentCapacityKey) as Long,
		type = (map.require(kIOPSTypeKey) as String).toPowerSourceType(),
	)
}

@OptIn(ExperimentalStdlibApi::class)
private fun String.toPowerSourceState() =
	IOPSPowerSourceState.entries.first { it.value == this }

@OptIn(ExperimentalStdlibApi::class)
private fun String.toPowerSourceType() =
	IOPSPowerSourceType.entries.first { it.value == this }

private fun Map<String, Any?>.require(key: String): Any =
	this[key] ?: throw NoSuchElementException("No '$key' was found in IOPSPowerSourceDescription")
