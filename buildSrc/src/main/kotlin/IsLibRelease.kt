import org.gradle.api.Project

val Project.isLibRelease: Boolean get() = properties["isLibRelease"].toString().toBoolean()
