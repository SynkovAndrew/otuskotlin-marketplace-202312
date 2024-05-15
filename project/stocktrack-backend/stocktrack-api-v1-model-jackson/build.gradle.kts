plugins {
    id("com.otus.otuskotlin.build.build-jvm")
    alias(libs.plugins.openapi.generator)
}

sourceSets {
    main {
        java.srcDir("${projectDir}/build/generated/src/main")
    }
}

dependencies {
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.17.0")
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310:2.17.0")
    implementation("com.fasterxml.jackson.core:jackson-databind:2.17.0")

    testImplementation(kotlin("test"))
}

val openApiVersion = "v1"
val specificationPath = "${projectDir.path}/../../openapi/api/${openApiVersion}/stock.api.yaml"

openApiGenerate {
    val openapiGroup = "${rootProject.group}.api.$openApiVersion"
    generatorName = "kotlin"
    library = "jvm-spring-webclient"
    packageName = openapiGroup
    apiPackage = "$openapiGroup.api"
    modelPackage = "$openapiGroup.models"
    inputSpec = specificationPath
    outputDir = "${projectDir.path}/build/generated"

    globalProperties.apply {
        put("models", "")
        put("modelDocs", "false")
    }
    configOptions.set(
        mapOf(
            "serializationLibrary" to "jackson",
            "dateLibrary" to "java8",
            "enumPropertyNaming" to "UPPERCASE",
            "collectionType" to "list"
        )
    )
}

openApiValidate {
    inputSpec = specificationPath
}

tasks.openApiGenerate {
    dependsOn(tasks.clean)
}

