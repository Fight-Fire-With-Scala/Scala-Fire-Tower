plugins {
    scala
    application
    id("org.openjfx.javafxplugin") version "0.0.13"
    id("com.github.eugenesy.scapegoat") version "0.2.0"
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

configure<com.github.eugenesy.scapegoat.ScapegoatExtension>  {
    scapegoatVersion = "1.4.8"
    scalaVersion = "2.12.13"
    dataDir = "${buildDir}/reports/scapegoat"
    disabledInspections = arrayListOf("ArrayEquals", "AvoidToMinusOne")
    ignoredFiles = emptyArray<String>().toList()
    consoleOutput = true
    verbose = true
    reports = arrayListOf<String>("html", "xml")
    sourcePrefix = "src/main/scala"
    minimalWarnLevel = "info"
    enable = true
    testEnable = true
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
