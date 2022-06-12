plugins {
    id("java")
}

group = "org.lee"
version = "1.0"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.8.1")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.8.1")
    implementation("org.springframework.boot:spring-boot-starter:2.5.1")
    implementation("org.springframework.boot:spring-boot-autoconfigure:2.5.1")
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}