plugins {
    java
}

group = "org.lee"
version = "1.0.0"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.9.0")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")


    implementation(libs.arrowVector)
    implementation(libs.arrowVector)
    implementation(libs.arrowMemoryCore)
    implementation(libs.arrowMemoryNetty)
    implementation(libs.arrowFlight)
    implementation(libs.arrowPlasma)
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}