package dev.kamshanski.powerwatch2

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Tray
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberTrayState
import androidx.compose.ui.window.rememberWindowState
import dev.kamshanski.powerwatch2.di.AppComponent
import dev.kamshanski.powerwatch2.info.ui.InfoScreen
import dev.kamshanski.powerwatch2.warning.ui.WarningWindowComponent
import java.awt.Dimension
import java.awt.Toolkit

private val appComponent by lazy { AppComponent() }

val SCREEN_SIZE: DpSize by lazy {
	val screenSize: Dimension = Toolkit.getDefaultToolkit().screenSize
	val width = screenSize.getWidth()
	val height = screenSize.getHeight()

	DpSize(width = width.dp, height = height.dp)
}

// TODO заменить application на JFrame и настроить там сокрытие иконки из таскбара
fun main() = application {
	LaunchedEffect(Unit) {
		System.setProperty("apple.awt.UIElement", "true")
		println("Hello world")
		println("Platform is ${Platform.current}")
	}

	MaterialTheme {
		var infoWindowShown by remember { mutableStateOf(true) }
		var monitorError by remember { mutableStateOf<String?>(null) }
		var monitorErrorExplanation by remember { mutableStateOf(false) }

		val trayState = rememberTrayState()
		Tray(
			state = trayState,
			icon = rememberVectorPainter(Icons.Default.Star),
			menu = {
				Item(if (infoWindowShown) "Hide" else "Show", onClick = { infoWindowShown = !infoWindowShown })
				if (monitorError != null) {
					Item("Show monitor error explanation", onClick = { monitorErrorExplanation = true })
				}
				Separator()
				Item("Shutdown", onClick = { exitApplication() })
			}
		)

		if (infoWindowShown) {
			val infoComponent = remember { appComponent.makeInfoComponent() }

			Window(
				onCloseRequest = { infoWindowShown = false },
				title = "Compose for Desktop",
				state = rememberWindowState(size = DpSize(width = 350.dp, height = 200.dp)),
			) {
				InfoScreen(infoComponent.presenter)
			}
		}

		WarningWindowComponent(appComponent, onShowFailureInTray = { monitorError = it })
	}
}