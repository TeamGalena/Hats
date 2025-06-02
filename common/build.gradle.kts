val mixin_extras_version: String by extra

common {
    dependOn(project(":core"))
}

dependencies {
    compileOnly("io.github.llamalad7:mixinextras-common:${mixin_extras_version}")
}