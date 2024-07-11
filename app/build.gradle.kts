plugins {
    scala
    application
    id("org.openjfx.javafxplugin") version "0.0.13"
    id("io.github.cosmicsilence.scalafix") version "0.2.2"
}

repositories { mavenCentral() }

dependencies {
    implementation(libs.scala.library)
    implementation(libs.tuprolog)
    implementation(libs.scalafx)
    implementation(libs.monix)
    implementation(libs.scala.logging)
    implementation(libs.logback)
    testImplementation(libs.scalatest)
}

javafx {
    version = "19"
    modules = listOf("javafx.controls", "javafx.media")
}

application {
    mainClass.set("it.unibo.launcher.Launcher")
}