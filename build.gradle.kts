buildscript {
    repositories {
        google()
        jcenter()
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    }
    dependencies {
        classpath("com.android.tools.build:gradle:4.0.2")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.4.21")
        classpath("com.jfrog.bintray.gradle:gradle-bintray-plugin:1.8.5")
        classpath("org.jetbrains.compose:compose-gradle-plugin:0.3.0-build141")
    }
}

plugins {
    NexusStaging
}

allprojects {
    tasks {
        withType<Delete> { delete(buildDir) }
    }

    repositories {
        google()
        jcenter()
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    }

//    tasks.withType(Javadoc) {
//        options.addStringOption("Xdoclint:none", "-quiet")
//        options.addStringOption("encoding", "UTF-8")
//    }
}
