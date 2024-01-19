# PowerWatch2

Kotlin Multiplatform Library

### Publish to MavenCentral

1) Registering a Sonatype account as described here: 
   https://dev.to/kotlin/how-to-build-and-publish-a-kotlin-multiplatform-library-going-public-4a8k
2) Add developer id, name, email and the project url to
   `/convention-plugins/src/main/kotlin/convention.publication.gradle.kts`
3) Add the secrets to `local.properties`:

```
signing.keyId=...
signing.password=...
signing.secretKeyRingFile=...
ossrhUsername=...
ossrhPassword=...
```

4) Run `./gradlew :dodo:publishAllPublicationsToSonatypeRepository`

### Build platform artifacts

#### JVM jar

- Run `./gradlew :powerwatch:jvmJar`
- Output: `/powerwatch/build/libs/powerwatch-jvm-1.0.jar`

#### Linux static library

- Run `./gradlew :powerwatch:linkReleaseStaticLinuxX64`
- Output: `/powerwatch/build/bin/linuxX64/releaseStatic/libpowerwatch.a`

#### Windows static library

- Run `./gradlew :powerwatch:linkReleaseStaticMingwX64`
- Output: `/powerwatch/build/bin/mingwX64/releaseStatic/libpowerwatch.a`
