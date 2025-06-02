plugins {
    id("com.possible-triangle.gradle") version ("0.2.7")
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

    enablePublishing  {
        repositories {
            mavenLocal()

            // TODO use env
            val nexusToken = System.getenv("NEXUS_TOKEN")
            val nexusUser = System.getenv("NEXUS_USER")
            if (nexusToken != null && nexusUser != null) {
                maven {
                    url = uri("https://registry.somethingcatchy.net/repository/maven-releases/")
                    credentials {
                        username = nexusUser
                        password = nexusToken
                    }
                }
            }
        }
    }
}

enableSonarQube()