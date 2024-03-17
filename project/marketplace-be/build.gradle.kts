plugins {
    id("com.otus.otuskotlin.marketplace.build-jvm")
    alias(libs.plugins.ktor)
    alias(libs.plugins.kotlin.serialization)
}

dependencies {
    implementation(libs.logback)
    implementation(libs.kotlin.json)
    implementation("io.ktor:ktor-server-content-negotiation-jvm")
    implementation("io.ktor:ktor-serialization-kotlinx-json-jvm")
    implementation("io.ktor:ktor-server-core-jvm")
    implementation("io.ktor:ktor-server-netty-jvm")
    implementation("io.ktor:ktor-server-auth")
    implementation("io.ktor:ktor-client-apache")
    implementation("io.ktor:ktor-client-okhttp-jvm")
    implementation("io.ktor:ktor-server-auth")
    implementation("io.ktor:ktor-server-auth-jwt")
    implementation("io.ktor:ktor-server-cors")
    testImplementation("io.ktor:ktor-server-tests-jvm")
    testImplementation(kotlin("test"))
}

buildJvm {
    mainClass = "com.otus.otuskotlin.marketplace.MarketplaceApplicationKt"
    jarName = "marketplace-be"
}


tasks.test {
    useJUnitPlatform()
}
