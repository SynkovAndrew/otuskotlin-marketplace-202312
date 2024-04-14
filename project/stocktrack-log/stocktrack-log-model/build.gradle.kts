plugins {
    id("com.otus.otuskotlin.build.build-jvm")
    alias(libs.plugins.openapi.generator)
    alias(libs.plugins.kotlin.serialization)
}

dependencies {
    implementation(libs.kotlinx.datetime)
    implementation(libs.kotlinx.serialization.core)
    implementation(libs.kotlinx.serialization.json)
    testImplementation(kotlin("test"))
}

sourceSets {
    main {
        kotlin.srcDir("${projectDir}/build/generate-resources/main/src/main/kotlin")
    }
}

val openApiVersion = "v1"
val specificationPath = "$projectDir/../../openapi/log/${openApiVersion}/log.api.yaml"

openApiGenerate {
    val openapiGroup = "${rootProject.group}.api.$openApiVersion"
    generatorName = "kotlin"
    packageName = openapiGroup
    apiPackage = "$openapiGroup.api"
    modelPackage = "$openapiGroup.models"
    inputSpec = specificationPath

    globalProperties.apply {
        put("models", "")
        put("modelDocs", "false")
    }
    configOptions.set(
        mapOf(
            "library" to "jvm-ktor",
            "dateLibrary" to "java8",
            "enumPropertyNaming" to "UPPERCASE",
            "collectionType" to "list",
            "serializationLibrary" to "kotlinx_serialization"
        )
    )
}

openApiValidate {
    inputSpec = specificationPath
}

tasks.openApiGenerate {
    dependsOn(tasks.clean)
}

tasks.test {
    useJUnitPlatform()
}
