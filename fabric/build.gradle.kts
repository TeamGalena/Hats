import net.fabricmc.loom.api.LoomGradleExtensionAPI

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
