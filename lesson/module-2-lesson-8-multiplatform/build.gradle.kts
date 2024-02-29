plugins {
    alias(libs.plugins.kotlin.multiplatform)
    java
}

kotlin {
    jvm {
        compilations.all {
            kotlinOptions.jvmTarget = "17"
        }
        withJava()
        testRuns["test"].executionTask.configure {
            useJUnitPlatform()
        }
    }

    js(IR) {
        browser {
            testTask {
//                useKarma {
//                    // Выбираем браузеры, на которых будет тестироваться
//                    useChrome()
//                    useFirefox()
//                }
                // Без этой настройки длительные тесты не отрабатывают
                useMocha {
                    timeout = "100s"
                }
            }
        }
    }
    linuxX64()
    macosArm64()

    sourceSets {
        commonMain {
            dependencies {
                implementation(kotlin("stdlib-common"))
                implementation(libs.coroutines.core)
            }
        }

        commonTest {
            dependencies {
                implementation(kotlin("test"))
                implementation(libs.coroutines.test)
                runtimeOnly(libs.kotlinx.datetime)
            }
        }

        jvmMain {

        }

        jvmTest {
            dependencies {
                implementation(kotlin("test"))
            }
        }

        jsMain {
            dependencies {
                implementation(npm("js-big-decimal", "~1.3.4"))
                implementation(npm("is-sorted", "~1.0.5"))
            }
        }
        jsTest {
            dependencies {
                implementation(kotlin("test"))
            }
        }

        nativeMain {
        }
        nativeTest {
        }
    }
}

dependencies {
    compileOnly("org.projectlombok:lombok:1.18.20")
    annotationProcessor("org.projectlombok:lombok:1.18.20")
}
