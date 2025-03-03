plugins {
    id("java")
}

group = "ru.university"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    implementation("com.fasterxml.jackson.core:jackson-databind:2.0.1")
    implementation("org.jfree:jfreechart:1.5.3")
}

tasks.test {
    useJUnitPlatform()
}

tasks.jar.configure {
    manifest {
        attributes("Main-Class" to "ru.university.Main")
    }

    from(configurations.compileClasspath.get().map { if (it.isDirectory) it else zipTree(it) })
}