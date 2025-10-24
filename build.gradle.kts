plugins {
    id("com.possible-triangle.core")
    id("com.possible-triangle.common") apply false
    id("com.possible-triangle.fabric") apply false
    id("com.possible-triangle.forge") apply false
}

subprojects {
    apply(plugin = "com.possible-triangle.core")

    upload {
        maven {
            name = "hats-${project.name}"
            nexus()
        }
    }
}

enableSonarQube()
enableSpotless()
