package dev.kamshanski.powerwatch2.power

import dev.kamshanski.powerwatch2.model.BatteryCharge
import dev.kamshanski.powerwatch2.model.BatteryLifeTime
import dev.kamshanski.powerwatch2.model.BatteryLifeTime.TimeUnit
import dev.kamshanski.powerwatch2.model.PowerStatus
import dev.kamshanski.powerwatch2.power.nativemapping.IOPSPowerSourceDescription
import kotlinx.cinterop.ExperimentalForeignApi
import platform.Foundation.CFBridgingRelease
import platform.IOKit.IOPSCopyPowerSourcesInfo
import platform.IOKit.IOPSCopyPowerSourcesList
import kotlin.math.roundToInt

//[{Is Present=true, Max Capacity=100, LPM Active=false, Power Source State=AC Power,      Is Charged=true, Current=-405, Battery Provides Time Remaining=true, Name=InternalBattery-0, Is Charging=false, DesignCycleCount=1000, Current Capacity=100, Transport Type=Internal, Hardware Serial Number=65FGHF5675G7689GHJ, Type=InternalBattery, BatteryHealthCondition=, BatteryHealth=Good, Optimized Battery Charging Engaged=false, Time to Empty=0 , Time to Full Charge=0, Power Source ID=123123123}]
//[{Is Present=true, Max Capacity=100, LPM Active=false, Power Source State=Battery Power,                  Current=0,    Battery Provides Time Remaining=true, Name=InternalBattery-0, Is Charging=false, DesignCycleCount=1000, Current Capacity=100, Transport Type=Internal, Hardware Serial Number=65FGHF5675G7689GHJ, Type=InternalBattery, BatteryHealthCondition=, BatteryHealth=Good, Optimized Battery Charging Engaged=false, Time to Empty=-1, Time to Full Charge=0, Power Source ID=123123123}]
@OptIn(ExperimentalForeignApi::class)
actual fun getPowerStatus(): PowerStatus {
	val snapshot = IOPSCopyPowerSourcesInfo()
	val sourcesRef = IOPSCopyPowerSourcesList(snapshot) ?: return PowerStatus.NoPowerSourcesFound("No IOPSCopyPowerSourcesList")
	val sources = (CFBridgingRelease(sourcesRef) as List<Map<String, Any?>?>)
		.filterNotNull()
		.map { IOPSPowerSourceDescription(it) }
//		.filter { it.isPresent }                    // Only connected power sources

	println(sources)
	if (sources.isEmpty()) {
		return PowerStatus.WireOnly
	}

	val chargingDevice = sources.firstOrNull { it.isCharging }
	if (chargingDevice != null) {
		return PowerStatus.Autonomous.Charging(batteryChargeOf(chargingDevice))
	}

	val (mostPoweredSource, largestBatteryCharge) = sources
		.map { it to batteryChargeOf(it) }
		.maxBy { (_, charge) -> (charge as? BatteryCharge.Percent)?.value ?: Int.MIN_VALUE }
	return PowerStatus.Autonomous.InUse(largestBatteryCharge, batteryLifeTimeOf(mostPoweredSource))
}

private const val ERROR_PERCENTAGE = -1

private val IOPSPowerSourceDescription.powerPercentage: Int
	get() = if (maxCapacity != 0L) {
		((currentCapacity * 100).toDouble() / maxCapacity.toDouble()).roundToInt()
	} else {
		ERROR_PERCENTAGE
	}

private fun batteryChargeOf(d: IOPSPowerSourceDescription): BatteryCharge =
	try {
		val percentage = d.powerPercentage
		if (percentage in 0..100) {
			BatteryCharge.Percent(percentage)
		} else {
			BatteryCharge.Unknown("Got wrong percentage: $percentage. IOPS: $d")
		}
	} catch (ex: Throwable) {
		BatteryCharge.Unknown("Error calculating percentage. IOPS: $d")
	}

private fun batteryLifeTimeOf(d: IOPSPowerSourceDescription): BatteryLifeTime =
	when (d.timeToEmpty) {
		null                   -> BatteryLifeTime.Undefined("Empty value")
		-1L                    -> BatteryLifeTime.Calculating
		in Long.MIN_VALUE..-2L -> BatteryLifeTime.Undefined("Unknown negative time")
		else                   -> BatteryLifeTime.Duration(d.timeToEmpty, TimeUnit.MINUTES)
	}
