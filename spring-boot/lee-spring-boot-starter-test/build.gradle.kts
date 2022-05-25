plugins {
    id("java")
}

group = "org.lee"
version = "1.0"

repositories {
    mavenCentral()
}

dependencies {
    implementation(files("lib/spring-boot-starter-1.0.jar"))
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.8.1")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.8.1")

    implementation("org.springframework.boot:spring-boot-starter:2.5.1")
//    implementation("org.springframework.boot:spring-boot-starter-web:2.5.1")
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}