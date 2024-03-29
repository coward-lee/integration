plugins {
    id("java")
}

dependencies {
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.8.1")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.8.1")
    implementation("io.projectreactor:reactor-core:3.4.6")

}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}