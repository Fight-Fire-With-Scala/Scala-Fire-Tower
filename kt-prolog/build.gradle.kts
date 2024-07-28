plugins {
    alias(libs.plugins.kotlin.jvm)
    `java-library`
}

group = "it.unibo"
version = "0.0.1"

repositories {
    mavenCentral()
}

dependencies {
    implementation(libs.tuprolog.core)

    implementation(libs.tuprolog.solve.classic)
    implementation(libs.tuprolog.dsl.core)
    implementation(libs.tuprolog.dsl.unify)
    implementation(libs.tuprolog.dsl.theory)
    implementation(libs.tuprolog.dsl.solve)

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