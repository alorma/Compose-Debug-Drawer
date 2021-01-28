plugins {
    id("io.codearte.nexus-staging")
}

nexusStaging {
    username = System.getenv("ossUser")
    password = System.getenv("ossToken")
    packageGroup = property("packageGroup").toString()
}
