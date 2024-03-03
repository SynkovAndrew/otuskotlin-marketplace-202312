plugins {
    alias(libs.plugins.kotlin.jvm)
    application
}

dependencies {
    implementation(libs.coroutines.core)
    implementation(libs.okhttp)
    implementation(libs.jackson)
    testImplementation(kotlin("test"))
}

application {
    mainClass.set("com.otus.otuskotlin.marketplace.MainKt")
}

tasks.test {
    useJUnitPlatform()
}
