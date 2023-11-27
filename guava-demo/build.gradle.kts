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
    testImplementation("org.testng:testng:7.1.0")
    runtimeOnly("org.junit.jupiter:junit-jupiter-engine")
    implementation("com.github.ben-manes.caffeine:caffeine:3.1.8")

    testImplementation(libs.checker.annotations)
    testImplementation(libs.errorprone.annotations)

    testImplementation(libs.joor)
    testImplementation(libs.ycsb) {
        isTransitive = false
    }
    testImplementation(libs.picocli)
    testImplementation(libs.jctools)
    testImplementation(libs.fastutil)
    testImplementation(libs.lincheck)
    testImplementation(libs.guava.testlib)
    testImplementation(libs.commons.lang3)
    testImplementation(libs.bundles.slf4j.test)
    testImplementation(libs.commons.collections4)
    testImplementation(libs.commons.collections4) {
        artifact {
            classifier = "tests"
        }
    }
    testImplementation(libs.eclipse.collections.testutils)

    testImplementation(libs.osgi.annotations)
    testImplementation(libs.mockito)
    testImplementation(libs.jakarta.inject)
    testImplementation(libs.jcache)
    testImplementation(libs.config)
    testImplementation(libs.guava)
    testImplementation(libs.jcache.guice)
    testImplementation(libs.truth.java8)
    testImplementation(libs.truth)
    testImplementation(libs.guava.testlib)
    testImplementation(libs.bundles.slf4j.nop)
    testImplementation(libs.slf4j.test)
    testImplementation(libs.jcache.tck)
    testImplementation(libs.awaitility)
    testImplementation(libs.fastutil)
    testImplementation(libs.joor)
    testImplementation(libs.picocli)
//    implementation("org.osgi:org.osgi.service.log:1.5.0")
    testImplementation(libs.bundles.osgi.test.compile)
    testImplementation(libs.bundles.osgi.test.runtime)
    testImplementation(libs.lincheck)
    testImplementation(libs.eclipse.collections.testutils)

    testImplementation(libs.jcache.tck) {
        artifact {
            classifier = "tests"
        }
    }

    implementation(libs.guava)
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}