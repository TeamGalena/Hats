val freecam_neoforge_version: String by extra

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
        modRuntimeOnly("maven.modrinth:freecam:${freecam_neoforge_version}")
    }
}