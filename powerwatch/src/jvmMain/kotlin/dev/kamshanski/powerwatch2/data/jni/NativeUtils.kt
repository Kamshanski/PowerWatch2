package dev.kamshanski.powerwatch2.data.jni

import java.io.File
import java.io.FileNotFoundException
import java.io.IOException
import java.nio.file.FileSystemNotFoundException
import java.nio.file.FileSystems
import java.nio.file.Files
import java.nio.file.ProviderNotFoundException
import java.nio.file.StandardCopyOption

/** Источник: https://github.com/adamheinrich/native-utils */
object NativeUtils {
	/**
	 * The minimum length a prefix for a file has to have according to [File.createTempFile]}.
	 */
	private const val MIN_PREFIX_LENGTH = 3
	const val NATIVE_FOLDER_PATH_PREFIX = "nativeutils"

	/**
	 * Temporary directory which will contain the DLLs.
	 */
	private var temporaryDir: File? = null

	/**
	 * Loads library from current JAR archive
	 *
	 * The file from JAR is copied into system temporary directory and then loaded. The temporary file is deleted after
	 * exiting.
	 * Method uses String as filename because the pathname is "abstract", not system-dependent.
	 *
	 * @param path The path of file inside JAR as absolute path (beginning with '/'), e.g. /package/File.ext
	 * @throws IOException If temporary file creation or read/write operation fails
	 * @throws IllegalArgumentException If source file (param path) does not exist
	 * @throws IllegalArgumentException If the path is not absolute or if the filename is shorter than three characters
	 * (restriction of [File.createTempFile]).
	 * @throws FileNotFoundException If the file could not be found inside the JAR.
	 */
	@Throws(IOException::class)
	fun loadLibraryFromJar(path: String?) {
		requireNotNull(path)
		require(path.startsWith("/")) { "The path has to be absolute (start with '/')." }

		// Obtain filename from path
		val parts = path.split("/".toRegex()).toTypedArray()
		val filename = if (parts.size > 1) parts[parts.size - 1] else null

		// Check if the filename is okay
		require(!(filename == null || filename.length < MIN_PREFIX_LENGTH)) { "The filename has to be at least 3 characters long." }

		// Prepare temporary file
		if (temporaryDir == null) {
			temporaryDir = createTempDirectory(NATIVE_FOLDER_PATH_PREFIX)
			temporaryDir!!.deleteOnExit()
		}
		val temp = File(temporaryDir, filename)
		try {
			val inputStream = NativeUtils::class.java.getResourceAsStream(path) ?: throw NullPointerException("Input stream is null")
			inputStream.use { inputStream ->
				Files.copy(inputStream, temp.toPath(), StandardCopyOption.REPLACE_EXISTING)
			}
		} catch (e: IOException) {
			temp.delete()
			throw e
		} catch (e: NullPointerException) {
			temp.delete()
			throw FileNotFoundException("File $path was not found inside JAR.")
		}
		try {
			System.load(temp.absolutePath)
		} finally {
			if (isPosixCompliant) {
				// Assume POSIX compliant file system, can be deleted after loading
				temp.delete()
			} else {
				// Assume non-POSIX, and don't delete until last file descriptor closed
				temp.deleteOnExit()
			}
		}
	}

	private val isPosixCompliant: Boolean
		private get() = try {
			FileSystems.getDefault()
				.supportedFileAttributeViews()
				.contains("posix")
		} catch (e: FileSystemNotFoundException) {
			false
		} catch (e: ProviderNotFoundException) {
			false
		} catch (e: SecurityException) {
			false
		}

	@Throws(IOException::class)
	private fun createTempDirectory(prefix: String): File {
		val tempDir = System.getProperty("java.io.tmpdir")
		val generatedDir = File(tempDir, prefix + System.nanoTime())
		if (!generatedDir.mkdir()) throw IOException("Failed to create temp directory " + generatedDir.name)
		return generatedDir
	}
}