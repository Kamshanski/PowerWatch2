package dev.kamshanski.powerwatch2.power

import kotlin.test.Test
import kotlin.test.assertIs

class Test {

	@Test
	fun get_power_status_on_Windows_EXPECT_PowerStatus_Autonomous() {
		val ps = getPowerStatus()

		assertIs<PowerStatus.Autonomous>(ps)
	}
}