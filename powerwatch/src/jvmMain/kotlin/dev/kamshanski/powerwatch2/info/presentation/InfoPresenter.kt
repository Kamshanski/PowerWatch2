package dev.kamshanski.powerwatch2.info.presentation

import dev.kamshanski.powerwatch2.data.PowerMonitor
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class InfoPresenter(
	private val scope: CoroutineScope,
	private val powerMonitor: PowerMonitor,
) {

	private val _state = MutableStateFlow<InfoState>(InfoState.Initial)
	val state = _state as StateFlow<InfoState>

	private var listenerJob: Job? = null

	fun startMonitoring() {
		if (!mayStartMonitoring(state.value)) {
			return
		}

		powerMonitor.launch()

		listenerJob?.cancel()
		listenerJob = scope.launch(
			CoroutineExceptionHandler { _, throwable -> _state.value = InfoState.Failure(throwable.stackTraceToString()) }
		) {
			powerMonitor.state.collect { newMonitorState ->
				val currentState = state.value
				val newMainState = when (currentState) {
					is InfoState.Content -> currentState.copy(monitorState = newMonitorState)
					is InfoState.Failure,
					is InfoState.Initial -> InfoState.Content(newMonitorState)
				}

				_state.value = newMainState
			}
		}
	}
}

private fun mayStartMonitoring(state: InfoState): Boolean =
	when (state) {
		is InfoState.Content -> false
		is InfoState.Failure,
		is InfoState.Initial -> true
	}