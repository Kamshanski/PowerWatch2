package dev.kamshanski.powerwatch2.warning.presentation

import dev.kamshanski.powerwatch2.data.MonitorState
import dev.kamshanski.powerwatch2.data.PowerMonitor
import dev.kamshanski.powerwatch2.power.BatteryCharge
import dev.kamshanski.powerwatch2.power.PowerStatus
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class WarningPresenter(
	private val scope: CoroutineScope,
	private val powerMonitor: PowerMonitor,
) {

	private val _state = MutableStateFlow<WarningState>(WarningState.Initial)
	val state = _state as StateFlow<WarningState>

	private var listenerJob: Job? = null

	fun startMonitoring() {
		powerMonitor.launch()

		listenerJob?.cancel()
		listenerJob = scope.launch(
			CoroutineExceptionHandler { _, throwable -> _state.value = WarningState.Failure(throwable.stackTraceToString()) }
		) {
			powerMonitor.state.collect { newMonitorState ->
				ensureActive()

				val currentState = state.value
				val newWarningState = when (currentState) {
					is WarningState.Failure -> {
						listenerJob?.cancel()
						return@collect
					}

					is WarningState.Initial,
					is WarningState.Warning,
					is WarningState.Hiding  -> when (newMonitorState) {
						is MonitorState.Data    -> defineWarningState(newMonitorState.powerStatus)
						is MonitorState.Failure -> {
							listenerJob?.cancel()
							WarningState.Failure(newMonitorState.message)
						}
						is MonitorState.Initial -> WarningState.Hiding
					}
				}

				_state.value = newWarningState
			}
		}
	}

	private fun defineWarningState(powerStatus: PowerStatus): WarningState {
		val inUseStatus = powerStatus as? PowerStatus.Autonomous.InUse ?: return WarningState.Hiding
		val charge = inUseStatus.charge as? BatteryCharge.Percent ?: return WarningState.Hiding
		return if (charge.value <= 10) {
			WarningState.Warning(charge, powerStatus.lifeTime)
		} else {
			WarningState.Hiding
		}
	}
}