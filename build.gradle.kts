plugins {
    id("com.possible-triangle.core")
    id("com.possible-triangle.vanilla") apply false
    id("com.possible-triangle.fabric") apply false
    id("com.possible-triangle.forge") apply false
}

subprojects {
    apply(plugin = "com.possible-triangle.core")

    mod.version = mod.version.get().substringAfter('-')

    upload {
        maven {
            name = "hats-${mod.minecraftVersion.get()}-${project.name}"
            nexus()
        }
    }
}

enableSonarQube()
enableSpotless()
