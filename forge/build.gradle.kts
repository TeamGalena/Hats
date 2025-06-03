val freecam_forge_version: String by extra

neoforge {
    enableMixins()

    dependOn(project(":core"))
    dependOn(project(":common"))
}

// issues with mixin extras
tasks.withType<Test> { enabled = false }
tasks.compileTestJava { enabled = false }

dependencies {
    if (!env.isCI) {
        modRuntimeOnly("maven.modrinth:freecam:${freecam_forge_version}")
    }
}