val mod_version: String by extra
val mc_version: String by extra

plugins {
    id("com.possible-triangle.gradle") version ("0.2.12")
}

mod {
    version = "$mc_version-$mod_version"
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

    tasks.withType<Jar> {
        exclude("**/*.bbmodel")
    }

    enablePublishing {
        name = "hats-${project.name}"

        repositories {
            if (env.isCI) nexus()
        }
    }
}

enableSonarQube()
enableSpotless()
