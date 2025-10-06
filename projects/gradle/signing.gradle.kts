apply(plugin = "signing")

fun isReleaseBuild(): Boolean = System.getenv("IS_RELEASE") == "true" || false

fun getSigningKeyId(): String = findProperty("SIGNING_KEY_ID")?.toString() ?: System.getenv("SIGNING_KEY_ID") ?: ""

fun getSigningKey(): String = findProperty("SIGNING_KEY")?.toString() ?: System.getenv("SIGNING_KEY") ?: ""

fun getSigningPassword(): String =
    findProperty("SIGNING_PASSWORD")?.toString() ?: System.getenv("SIGNING_PASSWORD") ?: ""

if (isReleaseBuild()) {

    tasks.withType<PublishToMavenLocal>().configureEach {
        dependsOn(tasks.withType<Sign>())
    }

    tasks.matching { it.name.endsWith("ToNmcpRepository") }.configureEach {
        dependsOn(tasks.withType<Sign>())
    }

    configure<SigningExtension> {
        val key = getSigningKey()
        if (key.isNotBlank()) {
            println("Using in-memory signing with key ID: ${key}")
            // Use in-memory signing if key is provided
            useInMemoryPgpKeys(
                getSigningKeyId(),
                key,
                getSigningPassword(),
            )
        } else {

            // Fallback to GPG command with explicit passphrase
            useGpgCmd()
            // Configure GPG to use the passphrase from properties
            val password = getSigningPassword()
            if (password.isNotBlank()) {
                extra["signing.gnupg.keyName"] = getSigningKeyId()
                extra["signing.gnupg.passphrase"] = password
            }
        }

        sign(extensions.getByType<PublishingExtension>().publications)
    }
}