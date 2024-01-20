package dev.kamshanski.powerwatch2

enum class Platform {
	WINDOWS,
	MAC_OS_ARM,
	MAC_OS_INTEL,
	;

	companion object {

		val current by lazy {
			val osName = System.getProperty("os.name")
			val isMac = osName.contains("mac", ignoreCase = true)
			val isArm = System.getProperty("os.arch") == "aarch64"
			when {
				!isMac          -> WINDOWS
				isMac && isArm  -> MAC_OS_ARM
				isMac && !isArm -> MAC_OS_INTEL
				else            -> error("Unknown platform")
			}
		}
	}
}