import net.minecraftforge.gradle.userdev.jarjar.JarJarProjectExtension

val mixin_extras_version: String by extra
val freecam_forge_version: String by extra

forge {
    enableMixins()

    dependOn(project(":core"))
    dependOn(project(":common"))
}

// issues with mixin extras
tasks.withType<Test> { enabled = false }
tasks.compileTestJava { enabled = false }

val jarJar = the<JarJarProjectExtension>()

dependencies {
    compileOnly(annotationProcessor("io.github.llamalad7:mixinextras-common:${mixin_extras_version}")!!)
    implementation("jarJar"("io.github.llamalad7:mixinextras-forge:${mixin_extras_version}")) {
        jarJar.ranged(this, "[${mixin_extras_version},)")
    }

    if (!env.isCI) {
        modRuntimeOnly("maven.modrinth:freecam:${freecam_forge_version}")
    }
}