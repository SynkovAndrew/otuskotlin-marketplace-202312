plugins {
    id("com.otus.otuskotlin.build.build-jvm")
}

dependencies {
    testImplementation(kotlin("test"))
}

buildJvm {
    mainClass = "com.otus.otuskotlin.sample.MainAppClassKt"
    jarName = "app"
}

tasks.test {
    useJUnitPlatform()
}
