import net.fabricmc.loom.api.LoomGradleExtensionAPI

val freecam_fabric_version: String by extra
val cloth_config_fabric_version: String by extra

fabric {
    enableMixins()

    dependOn(project(":core"))
    dependOn(project(":common"))
}

configure<LoomGradleExtensionAPI> {
    log4jConfigs.from(file("log4j.xml"))

    runs {
        val client = getByName("client")
        remove(client)

        for (i in 1..3) {
            register("client-$i") {
                inherit(client)
                configName = "Fabric Client $i"
                runDir("run/$i")
            }
        }
    }
}

dependencies {
    if (!env.isCI) {
        modRuntimeOnly("maven.modrinth:freecam:${freecam_fabric_version}")
        modRuntimeOnly("maven.modrinth:9s6osm5g:${cloth_config_fabric_version}")
    }
}

uploadToModrinth()