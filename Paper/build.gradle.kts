plugins {
    id("java")
}

repositories {
    mavenCentral()
    maven("https://repo.papermc.io/repository/maven-public/")
    maven("https://jitpack.io")
}

apply {
    apply(plugin = "com.github.johnrengelman.shadow")
}

dependencies {
    compileOnly("io.papermc.paper:paper-api:1.21.5-R0.1-SNAPSHOT")
    testImplementation(platform("org.junit:junit-bom:5.9.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")
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
    publications {
        create<MavenPublication>("maven") {
            pom {
                groupId = "de.verdox.vcore"
                artifactId = "paper"
                version = "1.0.0-SNAPSHOT"
                from(components["java"])
                developers {
                    developer {
                        id = "verdox"
                        name = "Lukas Jonsson"
                        email = "mail.ysp@web.de"
                    }
                }
            }
        }
    }
    repositories {
        maven {
            name = "verdox"
            url = uri("https://repo.verdox.de/snapshots")
            credentials {
                username = (findProperty("reposilite.verdox.user") ?: System.getenv("REPO_USER")).toString()
                password = (findProperty("reposilite.verdox.key") ?: System.getenv("REPO_PASSWORD")).toString()
            }
        }
    }
}
