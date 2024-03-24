plugins {
    id("com.otus.otuskotlin.build.build-jvm")
    alias(libs.plugins.ktor)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.openapi.generator)
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
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.17.0")
    testImplementation("io.ktor:ktor-server-tests-jvm")
    testImplementation(kotlin("test"))
}

sourceSets {
    main {
        kotlin.srcDir("${projectDir}/build/generate-resources/main/src/main/kotlin")
    }
}

openApiGenerate {
    val openApiVersion = "v1"
    val openapiGroup = "${rootProject.group}.api.$openApiVersion"
    generatorName = "kotlin"
    packageName = openapiGroup
    apiPackage = "$openapiGroup.api"
    modelPackage = "$openapiGroup.models"
    invokerPackage = "$openapiGroup.invoker"
    inputSpec = "$projectDir/../specification/stock.${openApiVersion}.openapi.yaml"

    globalProperties.apply {
        put("models", "")
        put("modelDocs", "false")
    }
    configOptions.set(
        mapOf(
            "dateLibrary" to "string",
            "enumPropertyNaming" to "UPPERCASE",
            "serializationLibrary" to "jackson",
            "collectionType" to "list"
        )
    )
}

buildJvm {
    mainClass = "com.otus.otuskotlin.stocktrack.StockTrackApplicationKt"
    jarName = "stocktrack-be"
}

tasks.test {
    useJUnitPlatform()
}
