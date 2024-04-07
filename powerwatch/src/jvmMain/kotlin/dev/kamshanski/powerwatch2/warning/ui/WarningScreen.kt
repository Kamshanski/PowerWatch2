package dev.kamshanski.powerwatch2.warning.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.ApplicationScope
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowPlacement
import androidx.compose.ui.window.rememberWindowState
import dev.kamshanski.powerwatch2.SCREEN_SIZE
import dev.kamshanski.powerwatch2.di.AppComponent
import dev.kamshanski.powerwatch2.power.BatteryLifeTime
import dev.kamshanski.powerwatch2.warning.presentation.WarningState
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Composable
fun ApplicationScope.WarningWindowComponent(appComponent: AppComponent, onShowFailureInTray: (String?) -> Unit) {
	val warningComponent by remember { mutableStateOf(appComponent.makeWarningComponent()) }
	// To keep state op progress bar
	val state by warningComponent.presenter.state.collectAsState()

	when (val state = state) {
		is WarningState.Failure -> {
			onShowFailureInTray(state.message)
		}

		is WarningState.Hiding,
		is WarningState.Initial -> {
			onShowFailureInTray(null)
		}

		is WarningState.Warning -> {
			onShowFailureInTray(null)

			Window(
				onCloseRequest = { },
				title = "Compose for Desktop",
				state = rememberWindowState(size = SCREEN_SIZE, placement = WindowPlacement.Fullscreen),
				alwaysOnTop = true,
				undecorated = true,
				resizable = false,
			) {
				Column(modifier = Modifier.fillMaxSize()) {
					Text(LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME))
					Spacer(Modifier.size(10.dp))
					val time = when (state.lifeTime) {
						is BatteryLifeTime.Calculating -> " (Оставшееся время вычисляется)"
						is BatteryLifeTime.Duration    -> " (Осталось ${state.lifeTime.value} ${state.lifeTime.unit})"
						is BatteryLifeTime.Undefined   -> ""
					}
					Text(modifier = Modifier.weight(1f), text = "Подключи провод или я убью комп\nОсталось ${state.charge.value}%$time")
				}
			}
		}
	}

	LaunchedEffect(Unit) {
		warningComponent.presenter.startMonitoring()
	}
}