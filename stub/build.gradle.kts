val logback_version: String by extra

plugins {
    kotlin("jvm")
    id("io.ktor.plugin") version "3.1.3"
}

application {
    mainClass.set("galena.ApiStubKt")
}

dependencies {
//    compileOnly("com.google.code.gson:gson:${gson_version}")
    implementation(project(":core"))

    implementation("io.ktor:ktor-server-content-negotiation")
    implementation("io.ktor:ktor-server-core")
    implementation("io.ktor:ktor-serialization-gson")
    implementation("io.ktor:ktor-server-netty")

    implementation("ch.qos.logback:logback-classic:${logback_version}")
}