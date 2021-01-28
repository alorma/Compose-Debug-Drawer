plugins {
    id("de.marcphilipp.nexus-publish")
    signing
    id("Dokka")
}

val dokkaJar by tasks.creating(Jar::class) {
    archiveClassifier.set("javadoc")
    dependsOn(tasks.dokkaHtml)
}

publishing {
    publications.withType<MavenPublication> {
        pom {
            name.set(property("pomName").toString())
            description.set(property("pomDescription").toString())
            url.set(property("pomUrl").toString())
            licenses {
                license {
                    name.set(property("pomLicenseName").toString())
                    url.set(property("pomLicenseUrl").toString())
                }
            }
            developers {
                developer {
                    id.set(property("pomDeveloperId").toString())
                    name.set(property("pomDeveloperName").toString())
                    email.set(property("pomDeveloperEmail").toString())
                }
            }
            scm {
                url.set(property("pomSMCurl").toString())
                connection.set(property("pomsmcConnection").toString())
                developerConnection.set(property("pomSMCdeveloperConnection").toString())
            }
        }
    }

    publications {
        publications.configureEach {
            if (this is MavenPublication) {
                artifact(dokkaJar)
            }
        }
    }
}

nexusPublishing {
    this.repositories {
        sonatype()
    }

    useStaging.set(isLibRelease)
}

signing {
    if (isLibRelease) {
        useGpgCmd()
        sign(publishing.publications)
    }
}
