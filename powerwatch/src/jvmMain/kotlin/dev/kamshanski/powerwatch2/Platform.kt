package dev.kamshanski.powerwatch2

enum class Platform {
	WINDOWS,
	MAC_OS,

	UNKNOWN,
	;

	companion object {

		val current by lazy {
			val osName = System.getProperty("os.name")
			when {
				osName.contains("windows", ignoreCase = true) -> WINDOWS
				osName.contains("mac", ignoreCase = true)     -> MAC_OS
				else                                          -> UNKNOWN
			}
		}
	}
}