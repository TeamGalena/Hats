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

### Develop

In a dev environment, every API call go against `http://localhost:8080/api/` instead. 
This `stub` subproject contains a fake API listening at that address, which can be used to test and verify the mods functionality.
It can be run using the included IDEA run config *"Stub API"*. There are two additional endpoints using the `DELETE` and `PUT` methods, 
which can be used to modify the fake data returned by the API for specific player UUIDs.

There is also an exported [Postman](https://www.postman.com/) collection under `stub/postman.json`, which has example requests for the Stub API.