plugins {
    id("java")
    id("org.jetbrains.intellij") version "1.10.1"
}

group = "com.my.code"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}
//    <version>2022.1.1</version>
//    <idea-version since-build="221" until-build="221.*"/>
// Configure Gradle IntelliJ Plugin
// Read more: https://plugins.jetbrains.com/docs/intellij/tools-gradle-intellij-plugin.html
intellij {
    version.set("2022.1.1")
    type.set("IC") // Target IDE Platform

    plugins.set(listOf(/* Plugin Dependencies */))
}

tasks {
    // Set the JVM compatibility versions
    withType<JavaCompile> {
        sourceCompatibility = "8"
        targetCompatibility = "8"
    }

    patchPluginXml {
        sinceBuild.set("201")
        untilBuild.set("231.*")
    }

    signPlugin {
        certificateChain.set(System.getenv("CERTIFICATE_CHAIN"))
        privateKey.set(System.getenv("PRIVATE_KEY"))
        password.set(System.getenv("PRIVATE_KEY_PASSWORD"))
    }

    publishPlugin {
        token.set("")
    }

}
