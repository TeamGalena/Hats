# Galena Hats

This repository contains the code of the built-in mod rendering tophats on supporters of Team Galena.
It is included in our mods and does not have to be installed separately

## How to include

1. Add the repository

```kotlin
repositories {
    maven {
        url = uri("https://registry.somethingcatchy.net/repository/maven-releases/")
        content {
            includeGroup("dev.galena")
        }
    }
}
```

2. Get latest version for your preferred from the [Releases Tab](https://github.com/TeamGalena/Hats/releases).

3. Add and include mod in build JAR file

### Forge

````kotlin
dependencies {
    implementation(fg.deobf(jarJar("dev.galena:hats-forge:${galena_hats_version}") {
        version {
            strictly("[${galena_hats_version},)")
            prefer(galena_hats_version)
        }
    }))
}
````

### Fabric

````kotlin
dependencies {
    modImplementation(include("dev.galena:hats-fabric:${galena_hats_version}"))
}
````

### Using `com.possible-triangle.gradle`
```kotlin
forge {
    includesMod("dev.galena:hats-forge:${galena_hats_version}")
}

// OR

fabric {
    includesMod("dev.galena:hats-forge:${galena_hats_version}")
}
```