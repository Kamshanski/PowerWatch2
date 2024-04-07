import dev.kamshanski.powerwatch2.json.JSON
import dev.kamshanski.powerwatch2.power.getPowerStatus
import dev.kamshanski.powerwatch2.result.NegotiationModel
import jni.JNIEnvVar
import jni.jobject
import jni.jstring
import kotlinx.cinterop.CPointer
import kotlinx.cinterop.cstr
import kotlinx.cinterop.invoke
import kotlinx.cinterop.memScoped
import kotlinx.cinterop.pointed
import kotlinx.serialization.encodeToString

@CName("Java_dev_kamshanski_powerwatch2_data_jni_PowerStatusServiceJniBridge_getPowerStateJsonInternal")
fun Java_dev_kamshanski_powerwatch2_data_jni_PowerStatusServiceJniBridge_getPowerStateJsonInternal(env: CPointer<JNIEnvVar>, obj: jobject): jstring {
	val result = getPowerStatusResult()
	val resultJson = JSON.encodeToString(result)

	val resultJString = memScoped {
		env.pointed.pointed?.NewStringUTF!!.invoke(env, resultJson.cstr.ptr)
	}

	return resultJString!!
}

internal fun getPowerStatusResult(): NegotiationModel =
	try {
		val status = getPowerStatus()
		NegotiationModel.PowerStatusResult(status)
	} catch (ex: Throwable) {
		NegotiationModel.Error(ex)
	}