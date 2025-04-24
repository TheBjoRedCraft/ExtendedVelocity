plugins {
    kotlin("jvm") version "2.1.20"
    kotlin("kapt") version "2.1.20"
    id("com.gradleup.shadow") version "9.0.0-beta12"
}

group = "dev.thebjoredcraft.extendedvelocity"
version = "1.0.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven {
        name = "papermc"
        url = uri("https://repo.papermc.io/repository/maven-public/")
    }
}

dependencies {
    compileOnly(libs.velocity.api)
    kapt("com.velocitypowered:velocity-api:3.4.0-SNAPSHOT")
}

kotlin {
    jvmToolchain(21)
}