val mixin_extras_version: String by extra

fabric {
    enableMixins()

    dependOn(project(":core"))
    dependOn(project(":common"))
}

dependencies {
    "include"(implementation(annotationProcessor("io.github.llamalad7:mixinextras-fabric:${mixin_extras_version}")!!)!!)
}

// TODO check if required, should be part of plugin?
sourceSets.main {
    resources.srcDir(project(":common").file("src/main/resources"))
    resources.srcDir(project(":common").file("src/generated/resources"))
}