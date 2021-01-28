import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

group = libGroup
version = generatedLibVersion

private val Project.libGroup: String get() = properties["libGroup"].toString()
private val Project.libVersion: String get() = properties["libVersion"].toString()
private val Project.generatedLibVersion get() = libVersion.generateVersion(isLibRelease)

fun String.generateVersion(isRelease: Boolean): String =
    "$this${if (isRelease) "" else ".$yyyyMMddHHmmss-SNAPSHOT"}"

private val yyyyMMddHHmmss: String
    get() = SimpleDateFormat("yyyyMMddHHmmss", Locale.US).format(Date())
