plugins {
    kotlin("jvm") version "2.1.20"
    kotlin("kapt") version "2.1.20"
    id("com.gradleup.shadow") version "9.0.0-beta12"
}

group = "dev.thebjoredcraft.extendedvelocity"
version = "1.1.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven {
        name = "papermc"
        url = uri("https://repo.papermc.io/repository/maven-public/")
    }

    maven {
        name = "william278"
        url = uri("https://repo.william278.net/velocity/")
    }
}

dependencies {
    compileOnly(libs.velocity.api)
    compileOnly(libs.velocity.proxy)
    kapt(libs.velocity.api)

    implementation(libs.bstats.api)
    implementation("io.netty:netty-buffer:5.0.0.Alpha2")
    implementation("net.objecthunter:exp4j:0.4.8")
}

tasks.shadowJar {
    relocate("org.bstats", "dev.thebjoredcraft.extendedvelocity.bstats")
}

kotlin {
    jvmToolchain(21)
}