package dev.kamshanski.powerwatch2.json

import dev.kamshanski.powerwatch2.power.BatteryCharge
import dev.kamshanski.powerwatch2.power.BatteryLifeTime
import dev.kamshanski.powerwatch2.power.PowerStatus
import dev.kamshanski.powerwatch2.result.NegotiationModel
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import kotlinx.serialization.modules.subclass

val JSON by lazy {
	Json {
		serializersModule = SerializersModule {
			polymorphic(BatteryLifeTime::class) {
				subclass(BatteryLifeTime.Duration::class)
				subclass(BatteryLifeTime.Calculating::class)
				subclass(BatteryLifeTime.Undefined::class)
			}
			polymorphic(BatteryCharge::class) {
				subclass(BatteryCharge.Percent::class)
				subclass(BatteryCharge.Unknown::class)
			}
			polymorphic(PowerStatus::class) {
				subclass(PowerStatus.Autonomous.InUse::class)
				subclass(PowerStatus.Autonomous.Charging::class)
				subclass(PowerStatus.NoPowerSourcesFound::class)
				subclass(PowerStatus.WireOnly::class)
			}
			polymorphic(NegotiationModel::class) {
				subclass(NegotiationModel.PowerStatusResult::class)
				subclass(NegotiationModel.Error::class)
			}
		}
	}
}