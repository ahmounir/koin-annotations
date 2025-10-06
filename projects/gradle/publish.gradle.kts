apply(plugin = "maven-publish")

val javadocJar = tasks.findByName("javadocJar")

configure<PublishingExtension> {
    publications {
        withType<MavenPublication> {
            if (javadocJar != null) artifact(javadocJar)
            pom {
                name.set("Koin")
                description.set("KOIN - Kotlin simple Dependency Injection Framework")
                url.set("https://insert-koin.io/")
                licenses {
                    license {
                        name.set("The Apache Software License, Version 2.0")
                        url.set("http://www.apache.org/licenses/LICENSE-2.0.txt")
                    }
                }
                scm {
                    url.set("https://github.com/ahmounir/koin-annotations")
                    connection.set("https://github.com/ahmounir/koin-annotations.git")
                }
                developers {
                    developer {
                        name.set("Ahmed Mounir")
                        email.set("your-email@example.com")  // Replace with your email
                    }
                }
            }
        }
    }
}

apply(from = file("../gradle/signing.gradle.kts"))