package dev.kamshanski.powerwatch2.data

import dev.kamshanski.powerwatch2.power.PowerStatus

sealed interface MonitorState {

	data object Initial : MonitorState
	data class Data(val powerStatus: PowerStatus) : MonitorState
	data class Failure(val message: String) : MonitorState
}