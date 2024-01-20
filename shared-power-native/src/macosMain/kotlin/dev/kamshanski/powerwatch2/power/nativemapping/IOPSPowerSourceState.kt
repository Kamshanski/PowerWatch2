package dev.kamshanski.powerwatch2.power.nativemapping

import platform.IOKit.kIOPSACPowerValue
import platform.IOKit.kIOPSBatteryPowerValue
import platform.IOKit.kIOPSOffLineValue

//[{Is Present=true, Max Capacity=100, LPM Active=false, Power Source State=AC Power,      Is Charged=true, Current=-405, Battery Provides Time Remaining=true, Name=InternalBattery-0, Is Charging=false, DesignCycleCount=1000, Current Capacity=100, Transport Type=Internal, Hardware Serial Number=D861353A1QRK7LNB0, Type=InternalBattery, BatteryHealthCondition=, BatteryHealth=Good, Optimized Battery Charging Engaged=false, Time to Empty=0 , Time to Full Charge=0, Power Source ID=7340131}]
enum class IOPSPowerSourceState(val value: String) {

	/** Power source is currently using the internal battery */
	BatteryPower(kIOPSBatteryPowerValue),

	/** Power source is connected to external or AC power, and is not draining the internal battery.*/
	ACPower(kIOPSACPowerValue),

	/** Power source is off-line or no longer connected */
	Offline(kIOPSOffLineValue);

	companion object {

	}
}