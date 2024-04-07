package dev.kamshanski.powerwatch2.info.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.movableContentOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import dev.kamshanski.powerwatch2.info.presentation.InfoState
import dev.kamshanski.powerwatch2.data.MonitorState
import dev.kamshanski.powerwatch2.power.PowerStatus
import dev.kamshanski.powerwatch2.info.presentation.InfoPresenter

@Composable
fun InfoScreen(presenter: InfoPresenter) {
	// To keep state op progress bar
	val ProgressIndicator = remember { movableContentOf { CircularProgressIndicator(Modifier.size(24.dp)) } }
	val state by presenter.state.collectAsState()

	Column(
		Modifier.fillMaxSize().background(Color.White).padding(16.dp).verticalScroll(rememberScrollState()),
		horizontalAlignment = Alignment.CenterHorizontally,
		verticalArrangement = Arrangement.Center
	) {
		when (val state = state) {
			is InfoState.Initial -> ProgressIndicator()
			is InfoState.Content -> when (val monitorState = state.monitorState) {
				is MonitorState.Data    -> WarningComponent(monitorState.powerStatus)
				is MonitorState.Failure -> ErrorContent(monitorState.message)
				is MonitorState.Initial -> ProgressIndicator()
			}

			is InfoState.Failure -> ErrorContent(state.message)
		}
		println("Rendered $state")
	}

	LaunchedEffect(Unit) {
		presenter.startMonitoring()
	}
}

@Composable
private fun ColumnScope.WarningComponent(powerStatus: PowerStatus) {
	when (powerStatus) {
		is PowerStatus.Autonomous.Charging -> Text("Charging")
		is PowerStatus.Autonomous.InUse    -> Text("In use")
		is PowerStatus.NoPowerSourcesFound -> Text("No power sources found error:\n${powerStatus.message}")
		is PowerStatus.WireOnly            -> Text("No battery. Only AC wire.\nInsert battery.")
	}
}

@Composable
private fun ColumnScope.ErrorContent(message: String) {
	Text(message, color = Color.Red)
}