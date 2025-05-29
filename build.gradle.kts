plugins {
    kotlin("jvm") version "2.1.20"
    kotlin("kapt") version "2.1.20"
    id("com.gradleup.shadow") version "9.0.0-beta12"
}

group = "dev.thebjoredcraft.extendedvelocity"
version = "1.1.0"

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
    implementation(libs.netty)
    implementation(libs.exp4j)
    implementation(libs.sqlite)
    implementation(libs.mariadb)
    implementation(libs.kotlin.coroutines)
    implementation(libs.bundles.mccoroutine)

    api(libs.bundles.exposed) {
        exclude("org.jetbrains.kotlin", "kotlin-stdlib")
        exclude("org.jetbrains.kotlin", "kotlin-reflect")
        exclude("org.jetbrains.kotlinx", "kotlinx-coroutines-core")
        exclude("org.slf4j", "slf4j-api")
    }

    api(libs.hikari)
}

tasks.shadowJar {
    relocate("org.bstats", "dev.thebjoredcraft.extendedvelocity.bstats")
    archiveFileName = "ExtendedVelocity-${project.version}-dev.jar"
    dependencies {
        exclude(dependency("org.jetbrains.kotlin:kotlin-stdlib"))
        exclude(dependency("org.jetbrains.kotlin:kotlin-stdlib-jdk8"))
        exclude(dependency("org.jetbrains.kotlin:kotlin-reflect"))
    }
}

kotlin {
    jvmToolchain(21)
}