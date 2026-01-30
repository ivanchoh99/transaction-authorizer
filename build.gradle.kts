plugins {
    id("java")
}

group = "org.example"
version = "1.0.0"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")

    // Jackson para procesamiento JSON
    implementation("com.fasterxml.jackson.core:jackson-databind:2.17.0")
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310:2.17.0")
}

tasks.test {
    useJUnitPlatform()
}

// Configuración del JAR para que sea ejecutable y contenga las librerías
tasks.jar {
    // 1. Nombre del archivo final
    archiveFileName.set("authorizer.jar")

    // 2. Definir el punto de entrada (Clase Main)
    manifest {
        attributes["Main-Class"] = "org.example.Main"
    }

    // 3. Empaquetar dependencias (Fat JAR)
    // Esto evita el error de "ClassNotFoundException" al ejecutar el jar
    from(configurations.runtimeClasspath.get().map { if (it.isDirectory) it else zipTree(it) })

    // 4. Estrategia para archivos duplicados (necesario para Jackson)
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
}