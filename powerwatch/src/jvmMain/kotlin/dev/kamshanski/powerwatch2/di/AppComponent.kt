package dev.kamshanski.powerwatch2.di

import dev.kamshanski.powerwatch2.data.PowerMonitor
import dev.kamshanski.powerwatch2.data.jni.PowerStatusServiceJniBridge
import dev.kamshanski.powerwatch2.json.JSON
import dev.kamshanski.powerwatch2.info.di.InfoComponent
import dev.kamshanski.powerwatch2.warning.di.WarningComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob

class AppComponent {

	private val json = JSON
	private val dataScope = CoroutineScope(Dispatchers.Default + SupervisorJob())
	private val powerStatusServiceJniBridge = PowerStatusServiceJniBridge()
	private val powerMonitor = PowerMonitor(dataScope, json, powerStatusServiceJniBridge)

	fun makeInfoComponent(): InfoComponent = InfoComponent(powerMonitor)
	fun makeWarningComponent(): WarningComponent = WarningComponent(powerMonitor)
}