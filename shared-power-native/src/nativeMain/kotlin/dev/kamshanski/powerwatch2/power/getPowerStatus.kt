package dev.kamshanski.powerwatch2.power

import dev.kamshanski.powerwatch2.result.NegotiationModel

internal expect fun getPowerStatus(): PowerStatus

fun loadPowerStatus(): NegotiationModel =
	try {
		val status = getPowerStatus()
		NegotiationModel.PowerStatusResult(status)
	} catch (ex: Throwable) {
		NegotiationModel.Error(ex)
	}