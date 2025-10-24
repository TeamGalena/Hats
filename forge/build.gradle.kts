val freecam_forge_version: String by extra

plugins {
    id("com.possible-triangle.forge")
}

forge {
    enableMixins()

    dependOn(project(":core"))
    dependOn(project(":common"))
}

dependencies {
    if (!env.isCI) {
        modRuntimeOnly("maven.modrinth:freecam:${freecam_forge_version}")
    }
}