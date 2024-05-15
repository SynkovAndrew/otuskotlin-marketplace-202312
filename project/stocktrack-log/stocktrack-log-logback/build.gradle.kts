plugins {
    id("com.otus.otuskotlin.build.build-jvm")
}

dependencies {
    implementation(project(":stocktrack-log-core"))

    implementation(kotlin("stdlib"))
    implementation(libs.slf4j.api)

    implementation(libs.logback)
    implementation(libs.logback.logstash)

//    implementation(libs.logback.appenders)
//    implementation(libs.fluentd)
}
