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
    implementation(libs.scalafx)
    implementation(libs.tuprolog)
    implementation(libs.monix)
    implementation(libs.scala.logging)
    implementation(libs.logback)
    implementation(libs.circe.yaml)
    implementation(libs.circe.generic)

    testImplementation(libs.scalatest)
    testImplementation(libs.junit)
    testImplementation(libs.scalatest.plusjunit)
}

tasks.withType(ScalaCompile::class.java) {
    scalaCompileOptions.additionalParameters =
        listOf(
            "-indent",
            "-rewrite",
            "-feature",
            "-Wunused:imports",
            "-language:implicitConversions"
        )

}

javafx {
    version = "21"
    modules = listOf("javafx.controls", "javafx.media", "javafx.fxml")
}

application {
    mainClass.set("it.unibo.launcher.Launcher")
}