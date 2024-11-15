plugins {
    id("java")
}

group = "de.verdox.vcore"
version = "1.0"
description = "paper"

repositories {
    mavenCentral()
    maven("https://repo.papermc.io/repository/maven-public/")
    maven("https://jitpack.io")
}

apply {
    apply(plugin = "com.github.johnrengelman.shadow")
}

dependencies {
    compileOnly("de.verdox.mccreativelab:mccreativelab-api:1.21-R0.1-SNAPSHOT")
    compileOnly("de.verdox.mccreativelab:plugin-extension:1.0")
    testImplementation(platform("org.junit:junit-bom:5.9.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testImplementation("com.github.MockBukkit:MockBukkit:v1.21-SNAPSHOT")
    implementation(rootProject)
}

tasks {
    named<com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar>("shadowJar") {
        archiveClassifier.set("") // Entfernt das "-all" Suffix
    }

    build {
        finalizedBy("shadowJar")
    }
}

tasks.test {
    useJUnitPlatform()
}

publishing {
    publications.create<MavenPublication>("maven").from(components["java"]);
    repositories.maven(repositories.mavenLocal())
}