plugins {
    alias(libs.plugins.kotlin.jvm)
    id("com.ncorti.ktfmt.gradle") version "0.19.0"
    `java-library`
}

group = "it.unibo"
version = "0.0.1"

repositories {
    mavenCentral()
}

ktfmt {
    kotlinLangStyle()
}

dependencies {
    implementation(libs.tuprolog.core)
    implementation(libs.tuprolog.unify)
    implementation(libs.tuprolog.theory)
    implementation(libs.tuprolog.solve)
    implementation(libs.tuprolog.solve.classic)
    implementation(libs.tuprolog.parser.core)
    implementation(libs.tuprolog.parser.theory)
    implementation(libs.tuprolog.dsl.core)
    implementation(libs.tuprolog.dsl.unify)
    implementation(libs.tuprolog.dsl.theory)
    implementation(libs.tuprolog.dsl.solve)

    implementation("it.unibo.tuprolog:examples:1.0.4")

    implementation(platform(libs.kotlin.bom))
    implementation(libs.kotlin.stdlib.jvm)

    implementation(kotlin("script-runtime"))
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")

}

tasks.test {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(17)
}
