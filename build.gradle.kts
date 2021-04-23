
plugins {
    `javiersc-nexus`
    `javiersc-docs`
}

version = checkNotNull(properties["libVersion"]?.toString())

buildscript {
    repositories {
        gradlePluginPortal()
        google()
        jcenter()
        mavenCentral()
    }
}

allprojects {
    repositories {
        google()
        jcenter()
        mavenCentral()
    }
}

tasks.withType<Delete>() {
    delete(rootProject.buildDir)
}
