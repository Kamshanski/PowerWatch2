package dev.kamshanski.powerwatch2.warning.di

import dev.kamshanski.powerwatch2.data.PowerMonitor
import dev.kamshanski.powerwatch2.warning.presentation.WarningPresenter
import kotlinx.coroutines.MainScope

class WarningComponent(
	powerMonitor: PowerMonitor,
) {

	private val presentationScope = MainScope()
	val presenter = WarningPresenter(presentationScope, powerMonitor)
}