package dev.kamshanski.powerwatch2.data

import dev.kamshanski.powerwatch2.data.jni.PowerStatusServiceJniBridge
import dev.kamshanski.powerwatch2.power.PowerStatus
import dev.kamshanski.powerwatch2.result.NegotiationModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.StringFormat
import kotlinx.serialization.decodeFromString

class PowerMonitor(
	private val scope: CoroutineScope,
	private val json: StringFormat,
	private val powerStatusServiceJniBridge: PowerStatusServiceJniBridge,
) {

	private val _state = MutableStateFlow<MonitorState>(MonitorState.Initial)
	val state = _state as StateFlow<MonitorState>

	private var monitorJob: Job? = null

	fun launch() {
		if (monitorJob != null) {
			return
		}

		monitorJob = scope.launch(
			CoroutineExceptionHandler { _, th ->
				_state.value = MonitorState.Failure(th.stackTraceToString())
			}
		) {
			powerStatusServiceJniBridge.initBridge()

			while (true) {
				val result = powerStatusServiceJniBridge.getPowerStateJson()
				val model = json.decodeFromString<NegotiationModel>(result)

				val newState = when (model) {
					is NegotiationModel.PowerStatusResult -> MonitorState.Data(model.value)
					is NegotiationModel.Error             -> MonitorState.Failure(model.message)
				}

				_state.value = newState

				delay(500)
			}
		}
	}
}