val logback_version: String by extra

plugins {
    kotlin("jvm")
    alias(libs.plugins.ktor)
}

application {
    mainClass.set("galena.ApiStubKt")
}

dependencies {
    implementation(project(":core"))

    implementation("io.ktor:ktor-server-content-negotiation")
    implementation("io.ktor:ktor-server-core")
    implementation("io.ktor:ktor-serialization-gson")
    implementation("io.ktor:ktor-server-netty")

    implementation(libs.logback)
}
