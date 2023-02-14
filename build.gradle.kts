plugins {
    alias(libs.plugins.kotlin.gradle.plugin)
    alias(libs.plugins.ktor.gradle.plugin)
}

group = "com.github.darmsteter"
version = "0.0.1"
application {
    mainClass.set("com.github.darmsteter.ApplicationKt")
}

class KtorJvmArgumentsProvider(providers: ProviderFactory) : CommandLineArgumentProvider {
    private val isDevelopment = providers.gradleProperty("development").map { it.toBoolean() }.orElse(false)

    override fun asArguments(): Iterable<String> {
        return listOf("-Dio.ktor.development=${isDevelopment.get()}")
    }
}

kotlin {
    jvmToolchain(17)
}

tasks {
    named<JavaExec>("run") {
        jvmArgumentProviders += KtorJvmArgumentsProvider(providers)
    }
}

dependencies {
    implementation(libs.ktor.server.core)
    implementation(libs.ktor.server.thymeleaf)
    implementation(libs.ktor.server.sessions)
    implementation(libs.ktor.server.auth)
    implementation(libs.ktor.server.netty)
    implementation(libs.ktor.client.core)
    implementation(libs.ktor.client.apache)
    implementation(libs.logback.classic)
    testImplementation(libs.ktor.server.tests)
    testImplementation(libs.kotlin.tests.junit5)
}