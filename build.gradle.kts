plugins {
    id("maven-publish")
    id("fabric-loom") version "1.16-SNAPSHOT"
}

version = "${project.property("mod_version")}+${stonecutter.current.version}"
group = "${project.property("maven_group")}"

base {
    archivesName = "${project.property("archives_base_name")}"
}

repositories {
    //ModMenu
    maven(url = "https://maven.terraformersmc.com/releases/")
    //Cloth Config
    maven(url = "https://maven.shedaniel.me/")
}

dependencies {
    minecraft("com.mojang:minecraft:${stonecutter.current.project}")
    mappings("net.fabricmc:yarn:${property("yarn_mappings")}:v2")
    modImplementation("net.fabricmc:fabric-loader:${project.property("loader_version")}")
    modImplementation("net.fabricmc.fabric-api:fabric-api:${property("fabric_version")}")

    //ModMenu
    modApi("com.terraformersmc:modmenu:${property("modmenu_version")}")

    //Cloth Config
    modApi("me.shedaniel.cloth:cloth-config-fabric:${property("clothconfig_version")}") {
        exclude(group = "net.fabricmc.fabric-api")
    }

    //Toml
    modImplementation("com.moandjiezana.toml:toml4j:${project.property("toml4j_version")}")
    include("com.moandjiezana.toml:toml4j:${project.property("toml4j_version")}")
}

tasks.processResources {
    inputs.property("version", project.version)
    inputs.property("minecraft", stonecutter.current.version)

    filesMatching("fabric.mod.json") {
        expand(
            "version" to project.version,
            "minecraft_version" to stonecutter.current.version,
            "loader_version" to "${project.property("loader_version")}"
        )
    }
}

loom {
    runConfigs.all {
        ideConfigGenerated(true) // Run configurations are not created for subprojects by default
        runDir = "../../run" // Use a shared run folder and create separate worlds
    }
}

tasks.withType<JavaCompile>().configureEach {
    options.release.set(21)
}

// Builds the version into a shared folder in `build/libs/${mod version}/`
tasks.register<Copy>("buildAndCollect") {
    group = "build"
    from(tasks.remapJar.map { it.archiveFile })
    into(rootProject.layout.buildDirectory.file("/libs/${project.property("mod_version")}"))
}
tasks.named("build") { dependsOn("buildAndCollect") }

java {
    withSourcesJar()

    targetCompatibility = JavaVersion.VERSION_21
    sourceCompatibility = JavaVersion.VERSION_21
}

tasks.jar {
    inputs.property("archivesName", project.base.archivesName)

    from("LICENSE") {
        rename { name ->
            "${name}_${inputs.properties["archivesName"]}"
        }
    }
}

publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            artifactId = "${project.property("archives_base_name")}"
            from(components["java"])
        }
    }

    repositories {
    }
}