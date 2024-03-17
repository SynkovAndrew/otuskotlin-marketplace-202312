plugins {
    id("com.otus.otuskotlin.marketplace.build-jvm")
}

dependencies {
    testImplementation(kotlin("test"))
}

buildJvm {
    mainClass = "com.otus.otuskotlin.marketplace.MainAppClassKt"
    jarName = "app"
}

tasks.test {
    useJUnitPlatform()
}
