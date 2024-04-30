plugins {
    id("com.otus.otuskotlin.build.build-jvm")
    alias(libs.plugins.spring.boot)
    alias(libs.plugins.spring.dependencies)
    alias(libs.plugins.spring.kotlin)
}

dependencies {
    implementation(project(":stocktrack-log-v1-mapper"))
    implementation(project(":stocktrack-internal-model"))

    implementation(project(":stocktrack-api-v1-model"))
    implementation(project(":stocktrack-api-v1-mapper"))

    implementation("com.otus.otuskotlin.stocktrack:stocktrack-log-core")
    implementation("com.otus.otuskotlin.stocktrack:stocktrack-log-logback")
    implementation("com.otus.otuskotlin.stocktrack:stocktrack-log-model")

    implementation("org.springframework.boot:spring-boot-starter")
    implementation("org.springframework.boot:spring-boot-starter-webflux")

    testImplementation(kotlin("test"))
}

buildJvm {
    mainClass = "com.otus.otuskotlin.stocktrack.StockTrackApplicationKt"
    jarName = "stocktrack-backend"
    dockerRepositoryOwner = "andrewsynkov"
}

tasks.test {
    useJUnitPlatform()
}
