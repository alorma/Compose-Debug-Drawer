plugins {
    `kotlin-dsl`
}

repositories {
    mavenCentral()
    jcenter()
    google()
    gradlePluginPortal()
}

val dokkaVersion = "1.4.20"
val nexusPublishVersion = "0.4.0"
val nexusStagingVersion = "0.22.0"

dependencies {
    implementation(gradleApi())
    implementation(localGroovy())

    implementation("org.jetbrains.dokka:dokka-core:$dokkaVersion")
    implementation("org.jetbrains.dokka:dokka-gradle-plugin:$dokkaVersion")
    implementation("io.codearte.gradle.nexus:gradle-nexus-staging-plugin:$nexusStagingVersion")
    implementation("de.marcphilipp.gradle:nexus-publish-plugin:$nexusPublishVersion")
}
