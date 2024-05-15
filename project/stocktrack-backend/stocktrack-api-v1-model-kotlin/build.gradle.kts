plugins {
    id("com.otus.otuskotlin.build.build-multiplatform")
    alias(libs.plugins.openapi.generator)
    alias(libs.plugins.kotlin.serialization)
}

kotlin {
    sourceSets {
        val commonMain by getting {
            kotlin.srcDir("${projectDir}/build/generated/src/commonMain")

            dependencies {
                implementation(libs.kotlinx.serialization.core)
                implementation(libs.kotlinx.serialization.json)
            }

        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
            }

        }
    }
}

val openApiVersion = "v1"
val specificationPath = "${projectDir.path}/../../openapi/api/${openApiVersion}/stock.api.yaml"

openApiGenerate {
    val openapiGroup = "${rootProject.group}.api.$openApiVersion"
    generatorName = "kotlin"
    packageName = openapiGroup
    apiPackage = "$openapiGroup.api"
    modelPackage = "$openapiGroup.models"
    inputSpec = specificationPath
    library = "multiplatform"
    outputDir = "${projectDir.path}/build/generated"
    templateDir = "${projectDir.path}/mustache"

    globalProperties.apply {
        put("models", "")
        put("modelDocs", "false")
    }
    configOptions.set(
        mapOf(
            "dateLibrary" to "kotlinx-datetime",
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

tasks.compileKotlinJvm {
    dependsOn(tasks.openApiGenerate)
}
