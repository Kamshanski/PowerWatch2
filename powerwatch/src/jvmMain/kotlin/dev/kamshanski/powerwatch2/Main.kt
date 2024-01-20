package dev.kamshanski.powerwatch2

import dev.kamshanski.powerwatch2.power.GetPowerStatus
import dev.kamshanski.powerwatch2.power.GetPowerStatus.Companion.InitState.InitResult

fun main() {
	println("Hello world")
	println("Platform is ${Platform.current}")

	when (val result = GetPowerStatus.initialize()) {
		is InitResult.Failure -> {
			println("Error((")
			result.ex.printStackTrace()
			println(result.ex)
			return
		}

		is InitResult.Success -> println("Success")
	}

	val getPowerStatus = GetPowerStatus()
	while (true) {
		Thread.sleep(400)
		val powerStatus = getPowerStatus.get()
		println("Got measurement")
		println(powerStatus)
	}
}