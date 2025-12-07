plugins {
    id("java")
    id("java-library")
    id("maven-publish")
}

group = "net.stachetopia.library"
version = "1.0"

publishing {
    repositories {
        maven {
            name = "GitHubPackages"
            url = uri("https://maven.pkg.github.com/stachetopia/stachepackages")
            credentials {
              username = project.findProperty("stachetopia.packages.publishing.username") as String?
              password = project.findProperty("stachetopia.packages.publishing.token") as String?
            }
        }
    }
    publications {
        create<MavenPublication>("gpr") {
            from(components["java"])
            artifactId = "celeritas"
        }
    }
}


repositories {
    mavenCentral()
    maven {
        url = uri("https://maven.pkg.github.com/stachetopia/stachecore")
        credentials {
            username = project.findProperty("stachetopia.packages.installing.username") as String?
            password = project.findProperty("stachetopia.packages.installing.token") as String?
        }
    }
    maven {
        url = uri("https://maven.pkg.github.com/stachetopia/stachepackages")
        credentials {
            username = project.findProperty("stachetopia.packages.installing.username") as String?
            password = project.findProperty("stachetopia.packages.installing.token") as String?
        }
    }
}


dependencies {
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")

    if (providers.gradleProperty("useLocalDeps").getOrElse("false").toBoolean()) {
        println("Using local dependencies for development")
        compileOnly(project(":stachecore"))
    } else {
        println("Using published dependencies")
        compileOnly("net.stachetopia:stachecore:1.0.4")
    }

}

tasks.test {
    useJUnitPlatform()
}

tasks.withType<JavaCompile> {
    options.release = 21
}