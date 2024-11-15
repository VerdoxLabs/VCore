package de.verdox.vcore.paper;

import io.papermc.paper.plugin.bootstrap.BootstrapContext;
import io.papermc.paper.plugin.loader.PluginClasspathBuilder;
import io.papermc.paper.plugin.loader.library.impl.MavenLibraryResolver;
import org.eclipse.aether.artifact.DefaultArtifact;
import org.eclipse.aether.graph.Dependency;
import org.eclipse.aether.repository.RemoteRepository;

public class VPipelineDependencyLoader {
    public static void load(PluginClasspathBuilder pluginClasspathBuilder){
        MavenLibraryResolver resolver = new MavenLibraryResolver();

        resolver.addDependency(new Dependency(new DefaultArtifact("commons-codec:commons-codec:1.15"), null));
        resolver.addDependency(new Dependency(new DefaultArtifact("commons-io:commons-io:2.11.0"), null));
        resolver.addDependency(new Dependency(new DefaultArtifact("com.google.guava:guava:31.1-jre"), null));
        resolver.addDependency(new Dependency(new DefaultArtifact("org.mongodb:mongo-java-driver:3.12.12"), null));
        resolver.addDependency(new Dependency(new DefaultArtifact("org.mongodb:bson:4.9.0"), null));
        resolver.addDependency(new Dependency(new DefaultArtifact("org.jetbrains:annotations:24.0.1"), null));
        resolver.addDependency(new Dependency(new DefaultArtifact("com.zaxxer:HikariCP:5.0.1"), null));

        resolver.addDependency(new Dependency(new DefaultArtifact("com.google.code.gson:gson:2.10.1"), null));
        resolver.addDependency(new Dependency(new DefaultArtifact("org.redisson:redisson-all:3.20.0"), null));
        resolver.addDependency(new Dependency(new DefaultArtifact("mysql:mysql-connector-java:8.0.33"), null));
        resolver.addDependency(new Dependency(new DefaultArtifact("org.reflections:reflections:0.10.2"), null));
        resolver.addDependency(new Dependency(new DefaultArtifact("org.apache.commons:commons-collections4:4.4"), null));

        resolver.addRepository(new RemoteRepository.Builder("maven", "default", "https://repo.maven.apache.org/maven2/.").build());
        resolver.addRepository(new RemoteRepository.Builder("jitpack", "default", "https://jitpack.io.").build());
        pluginClasspathBuilder.addLibrary(resolver);
    }
}
