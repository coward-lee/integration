plugins {
    java
}


dependencies {
    implementation("org.junit.jupiter:junit-jupiter-api:5.6.0")

    runtimeOnly("org.junit.jupiter:junit-jupiter-engine")
    implementation("com.github.ben-manes.caffeine:caffeine:3.1.8")
    testImplementation(libs.commons.lang3)
    implementation(libs.jctools)
    implementation(libs.guava)
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}