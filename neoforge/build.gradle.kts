val freecam_neoforge_version: String by extra

plugins {
    id("com.possible-triangle.neoforge")
}

neoforge {
    dependOn(project(":core"))
    dependOn(project(":common"))
}

dependencies {
    if (!env.isCI) {
        modRuntimeOnly("maven.modrinth:freecam:${freecam_neoforge_version}")
    }
}