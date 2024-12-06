plugins {
    java
    `maven-publish`

    // Nothing special about this, just keep it up to date
    //id("com.github.johnrengelman.shadow") version "8.1.1" apply false

    // In general, keep this version in sync with upstream. Sometimes a newer version than upstream might work, but an older version is extremely likely to break.
    id("com.github.johnrengelman.shadow") version "8.1.1" apply true
}

tasks.test {
    useJUnitPlatform()
}

allprojects {
    apply(plugin = "java-library")
    apply(plugin = "maven-publish")
    //apply(plugin = "io.papermc.paperweight.userdev")

    java {
        // Configure the java toolchain. This allows gradle to auto-provision JDK 17 on systems that only have JDK 8 installed for example.
        toolchain.languageVersion.set(JavaLanguageVersion.of(21))
        withSourcesJar()
        withJavadocJar()
    }

    repositories {
        mavenLocal()
        mavenCentral()
        maven {
            url = uri("https://papermc.io/repo/repository/maven-public/")
        }
        maven {
            name = "Verdox Reposilite"
            url = uri("https://repo.verdox.de/snapshots")
        }

        maven {
            url = uri("https://jitpack.io")
        }

        maven {
            url = uri("https://repo.maven.apache.org/maven2/")
        }
        maven {
            url = uri("https://mvn.lumine.io/repository/maven-public/")
        }
    }

    dependencies {
        implementation("de.verdox:vpipeline:1.0.4-SNAPSHOT")
        implementation("de.verdox:vserializer:1.0.5-SNAPSHOT")
        compileOnly("org.jetbrains:annotations:24.1.0")
        //paperweight.paperDevBundle("1.20.4-R0.1-SNAPSHOT")
        //compileOnly("de.verdox.vcore:VCore:1.0:all")

        //compileOnly("net.dv8tion:JDA:5.0.0-alpha.22")
        //compileOnly("io.lumine:Mythic-Dist:5.2.1")
        //compileOnly("com.comphenix.protocol:ProtocolLib:4.7.0")
        compileOnly("com.mojang:authlib:3.2.38")
        //implementation("com.github.zlataovce:takenaka:efa1b99173")
        compileOnly("com.jamieswhiteshirt:rtree-3i-lite:0.3.0")
        compileOnly("org.apache.commons:commons-collections4:4.4")

        testImplementation("org.apache.commons:commons-lang3:3.12.0")
        testImplementation("org.junit.jupiter:junit-jupiter:5.10.0")
        testImplementation("org.hamcrest:hamcrest:2.2")
        testImplementation("org.mockito:mockito-core:5.5.0")
        testImplementation("org.ow2.asm:asm-tree:9.5")

        testImplementation("de.verdox:vpipeline:1.0.4-SNAPSHOT")
        testImplementation("de.verdox:vserializer:1.0.5-SNAPSHOT")
        testImplementation("com.github.kstyrc:embedded-redis:0.6")
        testImplementation("org.jetbrains:annotations:24.1.0")
    }

    // Paper start - compile tests with -parameters for better junit parameterized test names
    tasks.compileTestJava {
        options.compilerArgs.add("-parameters")
    }

    tasks.test {
        useJUnitPlatform()
    }

    tasks {
        // Configure reobfJar to run when invoking the build task
/*        assemble {
            dependsOn(reobfJar)
        }*/

        build {
            dependsOn()
        }

        compileJava {
            options.encoding = Charsets.UTF_8.name() // We want UTF-8 for everything

            // Set the release flag. This configures what version bytecode the compiler will emit, as well as what JDK APIs are usable.
            // See https://openjdk.java.net/jeps/247 for more information.
            options.release.set(21)
        }
        /*    javadoc {
                options.encoding = Charsets.UTF_8.name() // We want UTF-8 for everything
            }*/



        processResources {
            filteringCharset = Charsets.UTF_8.name() // We want UTF-8 for everything
        }
    }
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            pom {
                groupId = "de.verdox.vcore"
                artifactId = "core"
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