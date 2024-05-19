plugins {
    id("com.otus.otuskotlin.build.build-jvm")
}

dependencies {
    implementation(kotlin("stdlib"))

    testImplementation(kotlin("test"))
    testImplementation(libs.coroutines.test)
}
