import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

// Note Kotlin version needs to be updated in both the buildscript and plugins.
// Dependencies will follow the buildscript

group = "org.biokotlin"

// This build script is need to use the early access
buildscript {
    val kotlinVersion by extra("1.9.24")

    repositories {
        mavenCentral()
        gradlePluginPortal()
    }

    dependencies {
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlinVersion")
        classpath(kotlin("serialization", version = kotlinVersion))
    }
}


plugins {
    val kotlinVersion = "1.9.24"
    java
    kotlin("jvm") version kotlinVersion
    kotlin("plugin.serialization") version kotlinVersion
    // Shadow allows for the creation of fat jars (all dependencies)
    id("com.github.johnrengelman.shadow") version "8.1.1"

    application
    `java-library`
}
apply {
    plugin("kotlinx-serialization")
}

repositories {
    mavenCentral()
    gradlePluginPortal()
    maven("https://maven.imagej.net/content/groups/public/")
    maven("https://jitpack.io")
    maven("https://dl.bintray.com/kotlin/kotlin-eap")
}

dependencies {

    val kotlinVersion = rootProject.extra["kotlinVersion"]

    implementation("org.biokotlin:biokotlin:0.25")

    implementation("org.apache.logging.log4j:log4j-core:2.23.1")
    implementation("org.apache.logging.log4j:log4j-api:2.23.1")

    implementation("com.github.ajalt.clikt:clikt:4.4.0")

    implementation("org.jetbrains.kotlin:kotlin-reflect:${kotlinVersion}")
    implementation("org.jetbrains.kotlin:kotlin-script-runtime:${kotlinVersion}")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.8.1")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.2.2")

    implementation("org.nield:kotlin-statistics:1.2.1")
    implementation("org.jetbrains.kotlinx:dataframe:0.8.0-rc-7")

    // Biology possible dependencies
    // Support fasta, bam, sam, vcf, bcf support
    implementation("com.github.samtools:htsjdk:4.0.1")

    implementation("org.apache.commons:commons-csv:1.8")

    implementation("com.google.guava:guava:33.1.0-jre")

    implementation("io.github.oshai:kotlin-logging-jvm:5.0.0")
    implementation("it.unimi.dsi:fastutil:8.5.12")

    testImplementation("org.junit.jupiter:junit-jupiter:5.8.0")

    val kotestVersion = "5.6.2"
    listOf("runner-junit5", "assertions-core", "property", "framework-datatest").forEach {
        testImplementation("io.kotest:kotest-$it-jvm:$kotestVersion")
    }

}

// include versions.properties file in jar file
tasks.jar {
    from(sourceSets.main.get().output)
    from(projectDir) {
        include("version.properties")
    }
}

java {
    withSourcesJar()
}

tasks.withType<KotlinCompile>().configureEach {
    kotlinOptions.jvmTarget = "17"
}

tasks {
    println("Source directories: ${sourceSets["main"].allSource.srcDirs}")
}

application {
    mainClass.set("biokotlin.cli.BiokotlinToolsKt")

    // Set name of generated scripts in bin/
    applicationName = "biokotlin-tools"
}

tasks.test {
    useJUnitPlatform()
    testLogging {
        events("passed", "skipped", "failed")
    }
}

tasks.jar {
    from(sourceSets.main.get().output)
}
