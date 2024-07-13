plugins {
    scala
    application
    alias(libs.plugins.javafx)
    alias(libs.plugins.scalafix)
    alias(libs.plugins.scoverage)
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
    testImplementation(libs.junit)
    testImplementation(libs.scalatest.plusjunit)
//    testRuntimeOnly("org.scala-lang.modules:scala-xml_3:2.0.1")
}

javafx {
    version = "19"
    modules = listOf("javafx.controls", "javafx.media", "javafx.fxml")
}

application {
    mainClass.set("it.unibo.launcher.Launcher")
}