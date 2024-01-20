package dev.kamshanski.powerwatch2.power

import dev.kamshanski.powerwatch2.model.PowerStatus
import dev.kamshanski.powerwatch2.power.getPowerStatus
import kotlin.test.Test
import kotlin.test.assertIs

class Test {

	@Test
	fun get_power_status_on_macOS_EXPECT_PowerStatus_Autonomous() {
		val ps = getPowerStatus()

		assertIs<PowerStatus.Autonomous>(ps)
	}
}