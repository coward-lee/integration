plugins {
    id("java")
}


dependencies {
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.8.1")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.8.1")

    implementation("io.netty:netty-all:4.1.67.Final")
    implementation("com.google.protobuf:protobuf-java:3.18.1")
    implementation("org.apache.logging.log4j:log4j-core:2.17.1")

    implementation("org.apache.zookeeper:zookeeper:3.7.0")
    implementation("org.apache.curator:curator-framework:5.2.0")

    compileOnly("org.projectlombok", "lombok", "1.18.20")
    runtimeOnly("com.google.protobuf:protobuf-gradle-plugin:0.8.17")
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}