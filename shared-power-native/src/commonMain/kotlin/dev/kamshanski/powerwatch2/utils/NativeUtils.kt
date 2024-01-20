package dev.kamshanski.powerwatch2.utils

internal inline fun Int.hasFlag(flag: Int): Boolean =
	this and flag != 0