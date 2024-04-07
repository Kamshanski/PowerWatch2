import dev.kamshanski.powerwatch2.json.JSON
import dev.kamshanski.powerwatch2.power.loadPowerStatus
import dev.kamshanski.powerwatch2.result.NegotiationModel
import kotlinx.serialization.encodeToString
import kotlin.test.Test
import kotlin.test.assertEquals

class Test {

	@Test
	fun `convert power status to json and decode json EXPECT decodes power status equals original result`() {
		val result = loadPowerStatus()
		val resultJson = JSON.encodeToString(result)
		val decodesResult = JSON.decodeFromString<NegotiationModel>(resultJson)
		println(result)
		println(resultJson)
		println(decodesResult)
		assertEquals(decodesResult, result)
	}
}