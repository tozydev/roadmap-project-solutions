plugins {
    application
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(libs.gson)
}

testing {
    suites {
        val test by getting(JvmTestSuite::class) {
            useJUnitJupiter("5.10.3")
        }
    }
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

application {
    mainClass = "io.github.tozydev.tasktracker.App"
}
