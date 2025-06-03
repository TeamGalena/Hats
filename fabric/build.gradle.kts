fabric {
    enableMixins()

    dependOn(project(":core"))
    dependOn(project(":common"))
}

// TODO check if required, should be part of plugin?
sourceSets.main {
    resources.srcDir(project(":common").file("src/main/resources"))
    resources.srcDir(project(":common").file("src/generated/resources"))
}