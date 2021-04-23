plugins {
    `kotlin-dsl`
}

repositories {
    gradlePluginPortal()
    google()
    jcenter()
    mavenCentral()
}

val javierscPlugins = "0.1.0-alpha.16"

dependencies {
    implementation("com.android.tools.build:gradle:7.0.0-alpha14")
    implementation("org.jetbrains.kotlin:kotlin-gradle-plugin:1.4.31")
    implementation("com.javiersc.gradle-plugins:docs:$javierscPlugins")
    implementation("com.javiersc.gradle-plugins:nexus:$javierscPlugins")
    implementation("com.javiersc.gradle-plugins:publish-android-library:$javierscPlugins")
}
