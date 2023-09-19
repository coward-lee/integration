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
    implementation("com.github.ben-manes.caffeine:caffeine:2.7.0")


    implementation(libs.guava)
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}