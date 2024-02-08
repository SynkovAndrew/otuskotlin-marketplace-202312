plugins {
    kotlin("jvm")
    application
}

val coroutinesVersion: String by project

dependencies {
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutinesVersion")
    implementation("com.squareup.okhttp3:okhttp:4.12.0") // http client
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.16.1") // from string to object
    testImplementation(kotlin("test"))
}

application {
    mainClass.set("com.otus.otuskotlin.marketplace.MainKt")
}

tasks.test {
    useJUnitPlatform()
}
