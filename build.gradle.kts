plugins {
    id("fabric-loom") version "1.3.0"
}

repositories {
    mavenCentral()
    maven("https://maven.fabricmc.net/")
}

group = "com.guishkaj"
version = "0.1.0"

base {
    archivesName.set("guishkaj")
}

dependencies {
    minecraft("com.mojang:minecraft:1.21.4")
    mappings("net.fabricmc:yarn:1.21.4+build.1:v2")
    modImplementation("net.fabricmc:fabric-api:0.89.0+1.21.4")
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

loom {
    runs {}
}

tasks.withType<org.gradle.api.tasks.compile.JavaCompile> {
    options.encoding = "UTF-8"
}
