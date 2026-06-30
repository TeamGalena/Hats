# Galena Hats

This repository contains the code of the built-in mod rendering tophats on supporters of Team Galena.
It is included in our mods and does not have to be installed separately

## How to include

![Latest Version](https://img.shields.io/maven-metadata/v?metadataUrl=https%3A%2F%2Fregistry.somethingcatchy.net%2Frepository%2Fmaven-releases%2Fdev%2Fgalena%2Fhats-26.1.2-core%2Fmaven-metadata.xml&label=latest%20version%20for%2026.1.2)

```kotlin
repositories {
    maven {
        url = uri("https://registry.somethingcatchy.net/repository/maven-public/")
        content {
            includeGroup("dev.galena")
        }
    }
}
```

### NeoForge

````kotlin
dependencies {
    implementation(jarJar("dev.galena:hats-26.1.2-neoforge:${galena_hats_version}") {
        version {
            strictly("[${galena_hats_version},)")
            prefer(galena_hats_version)
        }
    })
}
````

### Fabric

````kotlin
dependencies {
    modImplementation(include("dev.galena:hats-26.1.2-fabric:${galena_hats_version}"))
}
````

### Using `com.possible-triangle.*` gradle plugins
```kotlin
forge {
    includesMod("dev.galena:hats-26.1.2-neoforge:${galena_hats_version}")
}

// OR

fabric {
    includesMod("dev.galena:hats-26.1.2-fabric:${galena_hats_version}")
}
```

### Develop

In a dev environment, every API call go against `http://localhost:8080/api/` instead. 
This `stub` subproject contains a fake API listening at that address, which can be used to test and verify the mods functionality.
It can be run using the included IDEA run config *"Stub API"*. There are two additional endpoints using the `DELETE` and `PUT` methods, 
which can be used to modify the fake data returned by the API for specific player UUIDs.

There is also an exported [Postman](https://www.postman.com/) collection under `stub/postman.json`, which has example requests for the Stub API.
