plugins {
    id("com.possible-triangle.gradle") version ("0.2.17")
}

subprojects {
    repositories {
        modrinthMaven()

        maven {
            url = uri("https://jitpack.io")
            content {
                includeGroup("com.github.llamalad7.mixinextras")
            }
        }
    }

    enablePublishing {
        name = "hats-${project.name}"
        nexus()
    }
}

enableSonarQube()
enableSpotless()
