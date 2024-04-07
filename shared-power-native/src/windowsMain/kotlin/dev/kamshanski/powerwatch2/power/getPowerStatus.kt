package dev.kamshanski.powerwatch2.power

import dev.kamshanski.powerwatch2.power.BatteryLifeTime.TimeUnit
import dev.kamshanski.powerwatch2.utils.hasFlag
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.alloc
import kotlinx.cinterop.memScoped
import kotlinx.cinterop.pointed
import kotlinx.cinterop.ptr
import platform.windows.GetSystemPowerStatus
import platform.windows.SYSTEM_POWER_STATUS

@OptIn(ExperimentalForeignApi::class)
internal actual fun getPowerStatus(): PowerStatus =
	memScoped {
		val powerStatus = alloc<SYSTEM_POWER_STATUS>().ptr
		// Проверять return value не надо, т.к. функция может возвращать не 0 при несовершившейся ошибке
		GetSystemPowerStatus(powerStatus)

		convertToPowerStatus(powerStatus.pointed)
	}

private fun convertToPowerStatus(powerStatus: SYSTEM_POWER_STATUS): PowerStatus {
	val batteryFlag = powerStatus.BatteryFlag.toInt()
	val acLineStatus = powerStatus.ACLineStatus.toInt()
	return when {
		batteryFlag.hasFlag(128) -> PowerStatus.WireOnly
		acLineStatus == 0        -> PowerStatus.Autonomous.InUse(
			charge = convertPercent(powerStatus.BatteryLifePercent.toInt()),
			lifeTime = convertBatteryLifeTime(powerStatus.BatteryLifeTime.toLong())
		)

		acLineStatus == 1        -> PowerStatus.Autonomous.Charging(
			charge = convertPercent(powerStatus.BatteryLifePercent.toInt())
		)

		batteryFlag == 255       -> PowerStatus.NoPowerSourcesFound("Battery status is unknown")
		else                     -> PowerStatus.NoPowerSourcesFound("Battery status")
	}
}

private fun convertPercent(percent: Int): BatteryCharge =
	if (percent in 0..100) {
		BatteryCharge.Percent(percent)
	} else {
		BatteryCharge.Unknown("Got bad percentage: $percent")
	}

private fun convertBatteryLifeTime(seconds: Long) =
	if (seconds >= 0) {
		BatteryLifeTime.Duration(seconds, TimeUnit.SECONDS)
	} else {
		BatteryLifeTime.Undefined("Got bad time: $seconds")
	}