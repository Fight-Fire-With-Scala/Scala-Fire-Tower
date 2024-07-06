plugins {
    scala
    application
    id("org.openjfx.javafxplugin") version "0.0.13"
    id("cz.augi.gradle.wartremover") version "3.1.6"
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(libs.scala.library)
    implementation("org.scala-lang:scala3-library_3:3.3.1")
    implementation("org.scalafx:scalafx_3:21.0.0-R32")
    implementation("io.monix:monix_3:3.4.1")
    implementation("org.jfree:jfreechart:1.5.3")
}



javafx {
    version = "19"
    modules = listOf("javafx.controls", "javafx.media")
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

application {
    mainClass = "org.example.App"
}
