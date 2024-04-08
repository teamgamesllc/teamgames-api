import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

group = "com.teamgames"
version = "2.0.0"

plugins {
    id("java")
    id("com.github.johnrengelman.shadow") version "7.1.2"
}

tasks.compileJava {
    options.encoding = "UTF-8"
    sourceCompatibility = JavaVersion.VERSION_16.toString()
    targetCompatibility = JavaVersion.VERSION_16.toString()
}

sourceSets {
    main {
        java {
            srcDir("src")
        }
    }
}

tasks.withType<ShadowJar> {
    minimize()
    mergeServiceFiles()
    archiveVersion.set("2.0.0")
}