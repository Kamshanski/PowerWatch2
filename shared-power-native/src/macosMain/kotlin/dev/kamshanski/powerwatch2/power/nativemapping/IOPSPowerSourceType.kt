package dev.kamshanski.powerwatch2.power.nativemapping

import platform.IOKit.kIOPSInternalBatteryType
import platform.IOKit.kIOPSUPSType

enum class IOPSPowerSourceType(val value: String) {
	UPS(kIOPSUPSType),
	InternalBattery(kIOPSInternalBatteryType),
}