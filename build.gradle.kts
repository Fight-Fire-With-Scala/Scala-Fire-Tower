plugins {
    scala
    application
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(libs.scala.library)
    implementation("org.scala-lang:scala3-library_3:3.3.1")
    implementation("org.scalafx:scalafx_3:21.0.0-R32")
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

application {
    mainClass = "org.example.App"
}
