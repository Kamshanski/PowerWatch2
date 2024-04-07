package dev.kamshanski.powerwatch2.info.presentation

import dev.kamshanski.powerwatch2.data.MonitorState

sealed interface InfoState {

	data class Content(val monitorState: MonitorState) : InfoState

	data class Failure(val message: String) : InfoState

	data object Initial : InfoState
}
