plugins {
    java
}

group = "org.lee"
version = "1.0.0"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.junit.jupiter:junit-jupiter-api:5.6.0")
    runtimeOnly("org.junit.jupiter:junit-jupiter-engine")

    implementation(libs.guava)
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}