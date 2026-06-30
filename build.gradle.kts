plugins {
    id("com.possible-triangle.core")
    id("com.possible-triangle.common") apply false
    id("com.possible-triangle.fabric") apply false
    id("com.possible-triangle.neoforge") apply false
}

val (_, semver) =
    project.mod.version
        .get()
        .split("-")

mod.version = semver

subprojects {
    apply(plugin = "com.possible-triangle.core")

    upload {
        maven {
            name = "hats-${mod.minecraftVersion.get()}-${project.name}"
            nexus()
        }
    }
}

enableSonarQube()
enableSpotless()
