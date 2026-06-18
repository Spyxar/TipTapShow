pluginManagement {
    repositories {
        mavenCentral()
        gradlePluginPortal()
        maven(url = "https://maven.fabricmc.net/")
        maven(url = "https://maven.kikugie.dev/snapshots")
    }
    plugins {
        id("fabric-loom") version providers.gradleProperty("loom_version").get()
        id("net.fabricmc.fabric-loom") version providers.gradleProperty("loom_version").get()
    }
}

plugins {
    id("dev.kikugie.stonecutter") version "0.9"
}

stonecutter {
    centralScript = "build.gradle.kts"

    create(rootProject) {
        versions("1.21.11", "1.21.10", "1.21.8", "1.21.4")
        version("26.1").buildscript("unobfuscated.gradle.kts")
        version("26.1.1").buildscript("unobfuscated.gradle.kts")
        version("26.1.2").buildscript("unobfuscated.gradle.kts")
        version("26.2").buildscript("unobfuscated.gradle.kts")
        vcsVersion = "26.2"
    }
}