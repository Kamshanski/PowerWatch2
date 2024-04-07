package dev.kamshanski.powerwatch2.info.di

import dev.kamshanski.powerwatch2.data.PowerMonitor
import dev.kamshanski.powerwatch2.info.presentation.InfoPresenter
import kotlinx.coroutines.MainScope

class InfoComponent(
	powerMonitor: PowerMonitor,
) {

	private val presentationScope = MainScope()
	val presenter = InfoPresenter(presentationScope, powerMonitor)
}