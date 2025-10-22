plugins {
    id("com.possible-triangle.common")
}

common {
    dependOn(project(":core"))
}
